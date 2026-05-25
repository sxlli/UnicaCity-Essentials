package de.asxka.core.listener.Faction;

import de.asxka.core.UnicaCityEssentials;
import de.asxka.core.utils.PatternUtils;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.client.chat.ChatMessage;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;
import java.util.regex.Matcher;

public class ReinforcementListener {

  private final UnicaCityEssentials addon;
  public PatternUtils patternUtils = new PatternUtils();

  public ReinforcementListener(UnicaCityEssentials addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onChatMessage(ChatReceiveEvent event) {
    if (!this.addon.configuration().customReinf().enableCustomReinf().get()) {
      return;
    }

    ChatMessage message = event.chatMessage();

    // Schritt 1: Radikale Säuberung von Farbcodes (LegacySection ist am stabilsten)
    String raw = LegacyComponentSerializer.legacySection().serialize(message.component());
    String cleanText = raw.replaceAll("§[0-9a-fk-or]", "");

    // --- 1. ACCEPT LOGIK ---
    Matcher acceptMatcher = patternUtils.reinfAcceptPattern.matcher(cleanText);
    if (acceptMatcher.find()) {
      event.setCancelled(true); // Originalnachricht vom Server blockieren
      handleAccept(acceptMatcher);
      return;
    }

    // --- 2. REQUEST LOGIK ---
    Matcher requestMatcher = patternUtils.reinfPattern.matcher(cleanText);
    if (requestMatcher.find()) {
      event.setCancelled(true); // Originalnachricht vom Server blockieren
      handleRequest(requestMatcher);
    }
  }

  private void handleRequest(Matcher matcher) {
    String rawType = matcher.group(1);
    String playerName = matcher.group(2);
    String location = matcher.group(3);
    String distance = matcher.group(4);

    // Bereinige den Ort-String, falls gewünscht (z.B. "in der Nähe von Stadthalle" -> "Stadthalle")
    location = location.replaceAll("^(in der N.he von|an der|am|im|in der|bei der|beim|auf dem|auf der)\\s+", "").trim();

    String type = switch (rawType) {
      case "Medic benötigt!" -> "Medic";
      case "Dringend!" -> "Dringend";
      case "Drogenabnahme!" -> "Drogenabnahme";
      default -> "Normal";
    };

    String customFormat = addon.configuration().customReinf().CustomReinf().get();
    Component newMessage;

    if (customFormat != null && !customFormat.trim().isEmpty()) {
      String replacedFormat = customFormat
          .replace("%type%", type)
          .replace("%player%", playerName)
          .replace("%location%", location)
          .replace("%distance%", distance + "m");
      newMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(replacedFormat);
    } else {
      Component prefix = switch (type) {
        case "Medic" -> Component.text("Medic! ", NamedTextColor.RED).decorate(TextDecoration.BOLD);
        case "Dringend" -> Component.text("Dringend! ", NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD);
        case "Drogenabnahme" -> Component.text("Drogenabnahme! ", NamedTextColor.BLUE).decorate(TextDecoration.BOLD);
        default -> Component.text("Reinforcement! ", NamedTextColor.RED).decorate(TextDecoration.BOLD);
      };

      newMessage = Component.text()
          .append(prefix)
          .append(Component.text(playerName, NamedTextColor.AQUA))
          .append(Component.text(" - ", NamedTextColor.GRAY))
          .append(Component.text(location, NamedTextColor.AQUA))
          .append(Component.text(" - ", NamedTextColor.GRAY))
          .append(Component.text(distance + "m", NamedTextColor.DARK_AQUA))
          .build();
    }

    // Nachricht über den Executor lokal anzeigen
    this.addon.labyAPI().minecraft().chatExecutor().displayClientMessage(newMessage);
  }

  private void handleAccept(Matcher acceptMatcher) {
    String fullComerName = acceptMatcher.group(1).trim();
    String targetPlayer = acceptMatcher.group(2).trim();
    String distance = acceptMatcher.group(3);

    String faction = "";
    String acceptingPlayer = fullComerName;

    // Splittet "Polizei [UC]asxkaa" am letzten Leerzeichen
    if (fullComerName.contains(" ")) {
      int lastSpace = fullComerName.lastIndexOf(" ");
      faction = fullComerName.substring(0, lastSpace).trim() + " ";
      acceptingPlayer = fullComerName.substring(lastSpace + 1);
    }

    String customFormat = addon.configuration().customReinf().CustomReinfAccept().get();
    Component newMessage;

    if (customFormat != null && !customFormat.trim().isEmpty()) {
      String replacedFormat = customFormat
          .replace("%faction%", faction)
          .replace("%player%", acceptingPlayer)
          .replace("%target%", targetPlayer)
          .replace("%distance%", distance + "m");
      newMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(replacedFormat);
    } else {
      newMessage = Component.text()
          .append(Component.text("➥ ", NamedTextColor.GRAY))
          .append(Component.text(faction + acceptingPlayer, NamedTextColor.AQUA))
          .append(Component.text(" ➡ ", NamedTextColor.GRAY))
          .append(Component.text(targetPlayer, NamedTextColor.DARK_AQUA))
          .append(Component.text(" (", NamedTextColor.GRAY))
          .append(Component.text(distance + "m", NamedTextColor.DARK_AQUA))
          .append(Component.text(")", NamedTextColor.GRAY))
          .build();
    }

    // Nachricht über den Executor lokal anzeigen
    this.addon.labyAPI().minecraft().chatExecutor().displayClientMessage(newMessage);
  }
}