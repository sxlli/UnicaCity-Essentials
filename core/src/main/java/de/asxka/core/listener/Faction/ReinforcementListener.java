package de.asxka.core.listener.Faction;

import de.asxka.core.SolaraAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.client.chat.ChatMessage;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReinforcementListener {

  private final SolaraAddon addon;
  private final Pattern reinfPattern = Pattern.compile("(Unterstützung benötigt!|Medic benötigt!|Dringend!) (.+?) benötigt Unterstützung in der Nähe von (.+?)! \\((.+?) Meter entfernt\\)");

  public ReinforcementListener(SolaraAddon addon) {
    this.addon = addon;
  }

  private void extractRemaining(Component current, int matchedLength, int[] currentIndex, net.labymod.api.client.component.TextComponent.Builder builder) {
    if (current instanceof net.labymod.api.client.component.TextComponent textComponent) {
      String text = textComponent.getText();
      if (text != null && !text.isEmpty()) {
        if (currentIndex[0] >= matchedLength) {
          builder.append(current.plainCopy().style(current.style()));
        } else if (currentIndex[0] + text.length() > matchedLength) {
          int overlap = matchedLength - currentIndex[0];
          builder.append(Component.text(text.substring(overlap)).style(current.style()));
        }
        currentIndex[0] += text.length();
      }
    }
    for (Component child : current.getChildren()) {
      extractRemaining(child, matchedLength, currentIndex, builder);
    }
  }

  @Subscribe
  public void onChatMessage(ChatReceiveEvent event) {
    if (!this.addon.configuration().customReinf().enableCustomReinf().get()) {
      return;
    }

    ChatMessage message = event.chatMessage();
    String plainText = message.getPlainText();

    Matcher matcher = reinfPattern.matcher(plainText);
    if (matcher.find()) {
      String rawType = matcher.group(1); // z.B. "Unterstützung benötigt!", "Medic benötigt!" oder "Dringend!"
      String type;
      if (rawType.equals("Medic benötigt!")) {
        type = "Medic";
      } else if (rawType.equals("Dringend!")) {
        type = "Dringend";
      } else {
        type = "Normal";
      }

      String playerName = matcher.group(2);
      String location = matcher.group(3);
      String distance = matcher.group(4);

      // Eigene Nachricht aus der Config holen
      String customFormat = addon.configuration().customReinf().CustomReinf().get();

      Component newMessage;

      if (customFormat != null && !customFormat.trim().isEmpty()) {
        // Wenn der Nutzer etwas in den Einstellungen eingetragen hat, verwenden wir das:
        String replacedFormat = customFormat
            .replace("%type%", type)
            .replace("%player%", playerName)
            .replace("%location%", location)
            .replace("%distance%", distance + "m");

        // Versuchen, Farbcodes (&a, &c, etc.) zu übersetzen
        newMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(replacedFormat);
      } else {
        // Fallback: Die schöne Standard-Nachricht, je nach Typ angepasst
        Component prefixComponent;
        if (rawType.equals("Medic benötigt!")) {
          prefixComponent = Component.text("Medic! ", NamedTextColor.RED).decorate(TextDecoration.BOLD);
        } else if (rawType.equals("Dringend!")) {
          prefixComponent = Component.text("Dringend! ", NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD);
        } else {
          prefixComponent = Component.text("Reinforcement! ", NamedTextColor.RED).decorate(TextDecoration.BOLD);
        }

        newMessage = Component.text()
            .append(prefixComponent)
            .append(Component.text(playerName, NamedTextColor.AQUA))
            .append(Component.text(" \u2013 ", NamedTextColor.GRAY))
            .append(Component.text(location, NamedTextColor.AQUA))
            .append(Component.text(" \u2013 ", NamedTextColor.GRAY))
            .append(Component.text(distance + "m", NamedTextColor.DARK_AQUA))
            .build();
      }

      net.labymod.api.client.component.TextComponent.Builder finalBuilder = Component.text().append(newMessage);
      extractRemaining(message.component(), matcher.end(), new int[]{0}, finalBuilder);

      message.edit(finalBuilder.build());
    }
  }
}
