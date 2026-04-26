package de.asxka.core.listener.Faction;

import de.asxka.core.utils.GradientUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.asxka.core.utils.PatternUtils;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import de.asxka.core.UnicaCityEssentials;

public class FactionMemberDesignListener {
  private final UnicaCityEssentials addon;
  public PatternUtils patternUtils = new PatternUtils();

  private boolean capturingMembers = false;
  private long lastHeaderTime = 0L;

  public FactionMemberDesignListener(UnicaCityEssentials addon) {
      this.addon = addon;
  }

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    String plainMessage = event.chatMessage().getPlainText();
    if (plainMessage == null || plainMessage.trim().isEmpty()) {
      this.capturingMembers = false;
      return;
    }
    if (this.capturingMembers && System.currentTimeMillis() - this.lastHeaderTime > 200L) {
      this.capturingMembers = false;
    }
    if (plainMessage.contains("\n")) {
      String[] lines = plainMessage.split("\n");
      boolean hasHeader = false;
      for (String l : lines) {
        if (this.patternUtils.factionHeaderPattern.matcher(l.trim()).find()) {
          hasHeader = true;
          break;
        }
      }
      if (hasHeader) {
        TextComponent textComponent = Component.empty();
        this.capturingMembers = false;
        for (int i = 0; i < lines.length; i++) {
          Component lineComponent = processLine(lines[i].trim());
          Component textComponent1 = lineComponent;
          if (lineComponent == null) {
            textComponent1 = Component.text(lines[i].trim(), NamedTextColor.GRAY);
          }
          textComponent = textComponent.append(textComponent1);
          if (i < lines.length - 1) {
            textComponent = textComponent.append(Component.newline());
          }
        }
        event.setMessage(textComponent);
        this.capturingMembers = false;
        return;
      }
    }
    String cleanMessage = plainMessage.trim();
    Component processed = processLine(cleanMessage);
    if (processed != null) {
      event.setMessage(processed);
    }
  }

  private Component processLine(String line) {
    line = line.replaceFirst("^\\[?\\d{1,2}:\\d{2}:\\d{2}\\]?\\s*(»|\\|)?\\s*", "").trim();
    Matcher matcher = this.patternUtils.factionHeaderPattern.matcher(line);
    if (matcher.find()) {
      this.capturingMembers = true;
      this.lastHeaderTime = System.currentTimeMillis();
      String factionName = matcher.group(1);
      return Component.text("» ", NamedTextColor.DARK_GRAY)
        .append(GradientUtils.gradient("Fraktionsmitglieder ", TextColor.color(3503572), TextColor.color(4863444)))
        .append(Component.text("× ", NamedTextColor.DARK_GRAY))
        .append(getFactionComponent(factionName))
        .append(Component.text(" «", NamedTextColor.DARK_GRAY));
    }
    if (this.capturingMembers) {
      if (line.isEmpty() || line.startsWith("<") || line.startsWith("==") || line.contains("---")) {
        this.capturingMembers = false;
        return null;
      }
      if (line.contains("|")) {
        String[] parts = line.split("\\|", 2);
        String namePart = parts[0].trim();
        String statusPart = parts[1].trim();
        TextColor statusColor = NamedTextColor.GRAY;
        if (statusPart.contains("Nicht im Dienst")) {
          statusColor = NamedTextColor.RED;
        } else if (statusPart.contains("Im Dienst")) {
          statusColor = NamedTextColor.GREEN;
        } else if (statusPart.contains("AFK")) {
          statusColor = NamedTextColor.YELLOW;
        }
        return Component.text(" ▪ ", NamedTextColor.DARK_GRAY)
          .append(Component.text(namePart, NamedTextColor.GRAY))
          .append(Component.text(" | ", NamedTextColor.DARK_GRAY))
          .append(Component.text(statusPart, statusColor));
      }
      return Component.text(" ▪ ", NamedTextColor.DARK_GRAY)
        .append(Component.text(line, NamedTextColor.GRAY));
    }
    return null;
  }

  private Component getFactionComponent(String factionName) {
    TextColor color1, color2;
    String lowerCaseName = factionName.toLowerCase();
    if (lowerCaseName.contains("polizei")) {
      color1 = TextColor.color(232700);
      color2 = TextColor.color(542853);
    } else if (lowerCaseName.contains("fbi")) {
      color1 = TextColor.color(1517030);
      color2 = TextColor.color(857200);
    } else if (lowerCaseName.contains("rettungsdienst") || lowerCaseName.contains("rettung")) {
      color1 = TextColor.color(15407642);
      color2 = TextColor.color(8195855);
    } else if (lowerCaseName.contains("la cosa nostra") || lowerCaseName.contains("lcn")) {
      color1 = TextColor.color(6359305);
      color2 = TextColor.color(4195846);
    } else if (lowerCaseName.contains("yakuza")) {
      color1 = TextColor.color(14577576);
      color2 = TextColor.color(10041706);
    } else if (lowerCaseName.contains("calder")) {
      color1 = TextColor.color(13729066);
      color2 = TextColor.color(7227666);
    } else if (lowerCaseName.contains("ballas")) {
      color1 = TextColor.color(9317858);
      color2 = TextColor.color(4849888);
    } else {
      color1 = TextColor.color(4863444);
      color2 = TextColor.color(8861140);
    }
    return GradientUtils.gradient(factionName, color1, color2);
  }
}
