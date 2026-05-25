package de.asxka.core.listener.Faction;

import de.asxka.core.utils.GradientUtils;
import de.asxka.core.utils.PatternUtils;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

import java.util.regex.Matcher;

public class WantedMessagesListener {

  public PatternUtils patternUtils = new PatternUtils();

  private static String safeGroup(Matcher matcher, int index) {
    if (matcher.groupCount() < index) {
      return "";
    }
    String value = matcher.group(index);
    if (value != null) {
      value = value.replaceAll("\\[\\.\\]", "").trim();
    }
    return value == null ? "" : value.trim();
  }

    @Subscribe
    public void onChatReceive(ChatReceiveEvent event) {
        String plainMessage = event.chatMessage().getPlainText();
        if (plainMessage.isEmpty()) return;

        // BetterChat Copy-Buttons und andere Artefakte bereinigen
        plainMessage = plainMessage.replace("[.]", "").replace("📋", "").trim();

        // Verhindern, dass unsere selbst gesendeten Nachrichten erneut abgefangen und gelöscht werden!
        if (plainMessage.contains("ɢᴇꜱᴜᴄʜᴛ") || plainMessage.contains("ɢᴇᴛöᴛᴇᴛ") || plainMessage.contains("ᴀꜱꜱᴇʀᴠᴀᴛᴇɴᴋᴀᴍᴍᴇʀ")) return;

        boolean canceled = false;
        String[] lines = plainMessage.split("\n");

        // Wir prüfen zuerst, ob IRGENDEINE Zeile gematcht wird.
      for (String line : lines) {
        String cleanLine = line.trim();
        if (patternUtils.killedPattern.matcher(cleanLine).find() ||
            patternUtils.jailedPattern.matcher(cleanLine).find() ||
            patternUtils.reasonPattern.matcher(cleanLine).find() ||
            patternUtils.wantedReasonPattern.matcher(cleanLine).find() ||
            patternUtils.wantedLevelPattern.matcher(cleanLine).find() ||
            patternUtils.wantedClearedPattern.matcher(cleanLine).find() ||
            patternUtils.wantedChangePattern.matcher(cleanLine).find() ||
            patternUtils.wantedChangeAmountPattern.matcher(cleanLine).find() ||
            patternUtils.takeGunsPattern.matcher(cleanLine).find() ||
            patternUtils.takeDrugsPattern.matcher(cleanLine).find() ||
            patternUtils.fbankDepositPattern.matcher(cleanLine).find() ||
            patternUtils.fbankWithdrawPattern.matcher(cleanLine).find() ||
            patternUtils.asservatenkammerPattern.matcher(cleanLine).find()) {
          canceled = true;
          break;
        }
      }

        if (!canceled) {
            return; // Nichts gefunden, wir lassen die Nachricht in Ruhe!
        }

        event.setCancelled(true); // Originalnachricht ausblenden

        Component finalMessage = Component.empty();
        boolean isFirstLine = true;

        for (String line : lines) {
            String cleanLine = line.trim();
            Component newMessage = null;

            Matcher killMatcher = patternUtils.killedPattern.matcher(cleanLine);
            if (killMatcher.find()) {
                String victim = killMatcher.group(1);
                String killer = killMatcher.group(2);
                newMessage = Component.text()
                    .append(GradientUtils.gradient("ɢᴇᴛöᴛᴇᴛ", TextColor.color(0xbd2522), TextColor.color(0xcf6767)).decorate(TextDecoration.BOLD))
                    .append(Component.text(" ◆ ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(victim, NamedTextColor.BLUE))
                    .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(killer, NamedTextColor.BLUE))
                    .build();
            }

            Matcher jailMatcher = patternUtils.jailedPattern.matcher(cleanLine);
            if (newMessage == null && jailMatcher.find()) {
                String jailedPlayer = jailMatcher.group(1);
                String jailerPlayer = jailMatcher.group(2);
                newMessage = Component.text()
                    .append(GradientUtils.gradient("ɪɴʜᴀꜰᴛɪᴇʀᴛ", TextColor.color(0xbd2522), TextColor.color(0xcf6767)).decorate(TextDecoration.BOLD))
                    .append(Component.text(" ◆ ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(jailedPlayer, NamedTextColor.BLUE))
                    .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(jailerPlayer, NamedTextColor.BLUE))
                    .build();
            }

            Matcher reasonMatcher = patternUtils.reasonPattern.matcher(cleanLine);
            if (newMessage == null && reasonMatcher.find()) {
                String reason = safeGroup(reasonMatcher, 1);
                String time = safeGroup(reasonMatcher, 2);
                newMessage = Component.text()
                    .append(Component.text("  » ", NamedTextColor.DARK_GRAY))
                    .append(GradientUtils.gradient(reason, TextColor.color(0x3575d4), TextColor.color(0x4a35d4)).decorate(TextDecoration.BOLD))
                    .append(Component.text(" | ", NamedTextColor.DARK_GRAY))
                    .append(GradientUtils.gradient(time + " Minuten", TextColor.color(0x4a35d4), TextColor.color(0x3575d4)).decorate(TextDecoration.BOLD))
                    .build();
            }

            Matcher wantedReasonMatcher = patternUtils.wantedReasonPattern.matcher(cleanLine);
            if (newMessage == null && wantedReasonMatcher.find()) {
                String player = safeGroup(wantedReasonMatcher, 1);
                String reason = safeGroup(wantedReasonMatcher, 2);
                newMessage = Component.text()
                    .append(GradientUtils.gradient("ɢᴇꜱᴜᴄʜᴛ", TextColor.color(0xbd2522), TextColor.color(0xcf6767)).decorate(TextDecoration.BOLD))
                    .append(Component.text(" ◆ ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(player, NamedTextColor.BLUE))
                    .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(reason, NamedTextColor.BLUE))
                    .build();
            }

            Matcher wantedLevelMatcher = patternUtils.wantedLevelPattern.matcher(cleanLine);
            if (newMessage == null && wantedLevelMatcher.find()) {
                String player = safeGroup(wantedLevelMatcher, 1);
                String level = safeGroup(wantedLevelMatcher, 2);
                newMessage = Component.text()
                    .append(Component.text("  » ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(player + ": ", NamedTextColor.BLUE))
                    .append(GradientUtils.gradient(level + " Wanteds", TextColor.color(0x3575d4), TextColor.color(0x4a35d4)).decorate(TextDecoration.BOLD))
                    .build();
            }

            Matcher wantedClearedMatcher = patternUtils.wantedClearedPattern.matcher(cleanLine);
            if (newMessage == null && wantedClearedMatcher.find()) {
                String doer = safeGroup(wantedClearedMatcher, 1);
                String target = safeGroup(wantedClearedMatcher, 2);
                newMessage = Component.text()
                    .append(GradientUtils.gradient("ɢᴇʟöꜱᴄʜᴛ", TextColor.color(0xbd2522), TextColor.color(0xcf6767)).decorate(TextDecoration.BOLD))
                    .append(Component.text(" ◆ ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(target, NamedTextColor.BLUE))
                    .append(Component.text(" « ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(doer, NamedTextColor.BLUE))
                    .build();
            }

            Matcher wantedChangeMatcher = patternUtils.wantedChangePattern.matcher(cleanLine);
            if (newMessage == null && wantedChangeMatcher.find()) {
              String target = safeGroup(wantedChangeMatcher, 1);
              String state = safeGroup(wantedChangeMatcher, 2);
              newMessage = Component.text()
                  .append((GradientUtils.gradient("ᴠᴇʀäɴᴅᴇʀᴛ", TextColor.color(0xbd2522), TextColor.color(0xcf6767)).decorate(TextDecoration.BOLD)))
                  .append(Component.text(" ◆ ", NamedTextColor.DARK_GRAY))
                  .append(Component.text(state, NamedTextColor.BLUE))
                  .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                  .append(Component.text(target, NamedTextColor.BLUE))
                  .build();
            }

          Matcher wantedChangeAmountMatcher = patternUtils.wantedChangeAmountPattern.matcher(cleanLine);
          if (newMessage == null && wantedChangeAmountMatcher.find()) {
            String reason = safeGroup(wantedChangeAmountMatcher, 1);
            String oldAmount = safeGroup(wantedChangeAmountMatcher, 2); // Gruppe 2: Alte Punkte
            String newAmount = safeGroup(wantedChangeAmountMatcher, 3); // Gruppe 3: Neue Punkte

            newMessage = Component.text()
                .append(Component.text("  » ", NamedTextColor.DARK_GRAY))
                .append(GradientUtils.gradient(reason, TextColor.color(0x3575d4), TextColor.color(0x4a35d4)).decorate(TextDecoration.BOLD))
                .append(Component.text(" | ", NamedTextColor.DARK_GRAY))
                .append(Component.text(oldAmount, NamedTextColor.YELLOW).decorate(TextDecoration.BOLD))
                .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                .append(GradientUtils.gradient(newAmount + " Wanteds", TextColor.color(0x3575d4), TextColor.color(0x4a35d4)).decorate(TextDecoration.BOLD))
                .build();
          }

          Matcher takeGunsMatcher = patternUtils.takeGunsPattern.matcher(cleanLine);
          if (newMessage == null && takeGunsMatcher.find()) {
            String state = safeGroup(takeGunsMatcher, 1);
            String target = safeGroup(takeGunsMatcher, 2);

            newMessage = Component.text()
                .append((GradientUtils.gradient("ᴡᴀꜰꜰᴇɴ ᴀʙɴᴀʜᴍᴇ", TextColor.color(0xbd2522), TextColor.color(0xcf6767)).decorate(TextDecoration.BOLD)))
                .append(Component.text(" ◆ ", NamedTextColor.DARK_GRAY))
                .append(Component.text(state, NamedTextColor.BLUE))
                .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                .append(Component.text(target, NamedTextColor.BLUE))
                .build();
          }

          Matcher takeDrugsMatcher = patternUtils.takeDrugsPattern.matcher(cleanLine);
          if (newMessage == null && takeDrugsMatcher.find()) {
            String state = safeGroup(takeDrugsMatcher, 1);
            String target = safeGroup(takeDrugsMatcher, 2);

            newMessage = Component.text()
                .append((GradientUtils.gradient("ᴅʀᴏɢᴇɴ ᴀʙɴᴀʜᴍᴇ", TextColor.color(0xbd2522), TextColor.color(0xcf6767)).decorate(TextDecoration.BOLD)))
                .append(Component.text(" ◆ ", NamedTextColor.DARK_GRAY))
                .append(Component.text(state, NamedTextColor.BLUE))
                .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                .append(Component.text(target, NamedTextColor.BLUE))
                .build();
          }

          Matcher asservatenkammerMatcher = patternUtils.asservatenkammerPattern.matcher(cleanLine);
          if (newMessage == null && asservatenkammerMatcher.find()) {
            String player = safeGroup(asservatenkammerMatcher, 1);
            String item = safeGroup(asservatenkammerMatcher, 2);

            newMessage = Component.text()
                .append(GradientUtils.gradient("ᴀꜱꜱᴇʀᴠᴀᴛᴇɴᴋᴀᴍᴍᴇʀ", TextColor.color(0xbd2522), TextColor.color(0xcf6767)).decorate(TextDecoration.BOLD))
                .append(Component.text(" ◆ ", NamedTextColor.DARK_GRAY))
                .append(Component.text(player, NamedTextColor.BLUE))
                .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                .append(Component.text(item, NamedTextColor.AQUA))
                .build();
          }

          Matcher fbankDepositMatcher = patternUtils.fbankDepositPattern.matcher(cleanLine);
          if (newMessage == null && fbankDepositMatcher.find()) {
            String player = safeGroup(fbankDepositMatcher, 1);
            String amount = safeGroup(fbankDepositMatcher, 2);
            Component builder = Component.text()
                .append(GradientUtils.gradient("ꜰ-ʙᴀɴᴋ", TextColor.color(0x238c35), TextColor.color(0x3ac452)).decorate(TextDecoration.BOLD))
                .append(Component.text(" ◆ ", NamedTextColor.DARK_GRAY))
                .append(Component.text(player, NamedTextColor.BLUE))
                .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                .append(Component.text(amount + "$", NamedTextColor.GREEN))
                .build();

            for (String l : lines) {
                Matcher rMatcher = patternUtils.fbankReasonPattern.matcher(l.trim());
                if (rMatcher.find()) {
                    builder = builder.append(Component.text(" | ", NamedTextColor.DARK_GRAY))
                                    .append(Component.text("Grund: ", NamedTextColor.GRAY))
                                    .append(Component.text(safeGroup(rMatcher, 1), NamedTextColor.AQUA));
                    break;
                }
            }
            newMessage = builder;
          }

          Matcher fbankWithdrawMatcher = patternUtils.fbankWithdrawPattern.matcher(cleanLine);
          if (newMessage == null && fbankWithdrawMatcher.find()) {
            String player = safeGroup(fbankWithdrawMatcher, 1);
            String amount = safeGroup(fbankWithdrawMatcher, 2);

            Component builder = Component.text()
                .append(GradientUtils.gradient("F-BANK", TextColor.color(0xbd2522), TextColor.color(0xcf6767)).decorate(TextDecoration.BOLD))
                .append(Component.text(" ◆ ", NamedTextColor.DARK_GRAY))
                .append(Component.text(player, NamedTextColor.BLUE))
                .append(Component.text(" « ", NamedTextColor.DARK_GRAY))
                .append(Component.text(amount + "$", NamedTextColor.RED))
                .build();

            for (String l : lines) {
                Matcher rMatcher = patternUtils.fbankReasonPattern.matcher(l.trim());
                if (rMatcher.find()) {
                    builder = builder.append(Component.text(" | ", NamedTextColor.DARK_GRAY))
                                    .append(Component.text("Grund: ", NamedTextColor.GRAY))
                                    .append(Component.text(safeGroup(rMatcher, 1), NamedTextColor.AQUA));
                    break;
                }
            }
            newMessage = builder;
          }

          Matcher fbankReasonMatcher = patternUtils.fbankReasonPattern.matcher(cleanLine);
          if (newMessage == null && fbankReasonMatcher.find()) {
            continue; // Skip this line, it is combined with the F-Bank message
          }

            if (!isFirstLine) {
                finalMessage = finalMessage.append(Component.text("\n"));
            }
            if (newMessage != null) {
                finalMessage = finalMessage.append(newMessage);
            } else {
                // Falls diese eine Zeile im Block nicht gematcht hat, einfach unformatiert anhängen
                finalMessage = finalMessage.append(Component.text(cleanLine));
            }
            isFirstLine = false;
        }

        Laby.labyAPI().minecraft().chatExecutor().displayClientMessage(finalMessage);
    }
}
