package de.asxka.core.listener.Faction;

import de.asxka.core.utils.GradientUtils;
import de.asxka.core.utils.PatternUtils;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.Laby;
import net.labymod.api.util.concurrent.task.Task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import de.asxka.core.UnicaCityEssentials;
import de.asxka.core.configurations.enums.FactionType;
import de.asxka.core.utils.FactionCache;

public class FactionSyncListener {

    private final UnicaCityEssentials addon;
    private boolean capturingMembers = false;
    private long lastHeaderTime = 0;
    private String capturingFactionName = "";
    public PatternUtils patternUtils;

    private boolean isAutoSync = false;

    private final List<String> tempMembers = new ArrayList<>();

    public FactionSyncListener(UnicaCityEssentials addon) {
        this.addon = addon;
    }

    @Subscribe
    public void onServerJoin(ServerJoinEvent event) {
        if (!addon.configuration().factionMemberColor().enableFactionMemberColor().get()) {
            return;
        }

        if (addon.configuration().factionMemberColor().faction().get() == FactionType.ZIVILIST) {
            if (!de.asxka.core.utils.FactionCache.getMembers().isEmpty()) {
                de.asxka.core.utils.FactionCache.save(new ArrayList<>());
            }
            return; // Zivilisten haben keine Fraktion
        }

        // Nur beim allerersten Join ausführen, wenn wir noch nie gesynced haben, oder die Liste komplett leer ist.
        if (!FactionCache.hasSyncedOnce() || FactionCache.getMembers().isEmpty()) {
            // 3 Sekunden nach dem Joinen den Befehl ausführen
            Task.builder(() -> {
                isAutoSync = true;
                String cmdName = addon.configuration().factionMemberColor().faction().get().getCommandName();
                Laby.labyAPI().minecraft().chatExecutor().chat("/memberinfoall " + cmdName, false);
            }).delay(3000, TimeUnit.MILLISECONDS).build().execute();
        }

        if (addon.configuration().factionMemberColor().enableAllianceMemberColor().get()) {
            if (addon.configuration().factionMemberColor().allianceFaction().get() != FactionType.ZIVILIST) {
                if (!de.asxka.core.utils.AllianceCache.hasSyncedOnce() || de.asxka.core.utils.AllianceCache.getMembers().isEmpty()) {
                    Task.builder(() -> {
                        isAutoSync = true;
                        String cmdName = addon.configuration().factionMemberColor().allianceFaction().get().getCommandName();
                        Laby.labyAPI().minecraft().chatExecutor().chat("/memberinfoall " + cmdName, false);
                    }).delay(4500, TimeUnit.MILLISECONDS).build().execute();
                }
            } else {
                if (!de.asxka.core.utils.AllianceCache.getMembers().isEmpty()) {
                    de.asxka.core.utils.AllianceCache.save(new ArrayList<>());
                }
            }
        }
    }

    @Subscribe
    public void onChatReceive(ChatReceiveEvent event) {
        String plainMessage = event.chatMessage().getPlainText();
        if (plainMessage == null || plainMessage.trim().isEmpty()) {
            if (capturingMembers) {
                finishCapturing();
            }
            return;
        }

        if (capturingMembers && System.currentTimeMillis() - lastHeaderTime > 1500) {
            finishCapturing();
        }

        // Falls der Server die gesamte Liste als EINE einzige Nachricht (mit Absätzen) sendet:
        if (plainMessage.contains("\n")) {
            String[] lines = plainMessage.split("\n");
            boolean hasHeader = false;
            for (String l : lines) {
                if (patternUtils.factionallHeaderPattern.matcher(l.trim()).find()) {
                    hasHeader = true;
                    break;
                }
            }

            if (hasHeader) {
                if (capturingMembers) finishCapturing(); // Zurücksetzen für den neuen Block

                for (int i = 0; i < lines.length; i++) {
                    processLineRaw(lines[i].trim());
                }
                // Wir bearbeiten die Original-Nachricht diesmal NICHT,
                // um visuelle Bugs durch Falsch-Muster zu vermeiden.
                finishCapturing();
                return;
            }
        }

        // Falls der Server die Liste als einzelne, abgetrennte Nachrichten sendet:
        String cleanMessage = plainMessage.trim();
        processLineRaw(cleanMessage);
    }

    private void processLineRaw(String line) {
        line = line.replaceFirst("^\\[?\\d{1,2}:\\d{2}:\\d{2}\\]?\\s*(»|\\|)?\\s*", "").trim();
        Matcher matcher = patternUtils.factionallHeaderPattern.matcher(line);
        if (matcher.find()) {
            capturingMembers = true;
            capturingFactionName = matcher.group(1).trim();
            lastHeaderTime = System.currentTimeMillis();
            tempMembers.clear(); // Liste leeren für neue Abfrage

            // Start einen Timeout, falls der Chat aufhört
            Task.builder(() -> {
                if (capturingMembers) finishCapturing();
            }).delay(1500, TimeUnit.MILLISECONDS).build().execute();
            return;
        }

        if (capturingMembers) {
            // Stop capturing based on usual chat formats
            if (line.isEmpty() || line.startsWith("[") || line.startsWith("<") || line.startsWith("==")) {
                finishCapturing();
                return;
            }

            // Auslesen der Rank-Zeilen "- 6 | Feliix101, Samraa"
            if (line.startsWith("- ")) {
                String[] parts = line.split("\\|", 2);
                if (parts.length > 1) {
                    String namesPart = parts[1].trim();
                    for (String name : namesPart.split(",")) {
                        String clean = name.trim().replaceAll("§[0-9a-fk-or]", "");
                        if (!clean.isEmpty()) tempMembers.add(clean);
                    }
                }
            } else if (line.contains(",")) {
                // Continuation line ohne Prefix "Name1, Name2"
                for (String name : line.split(",")) {
                    String clean = name.trim().replaceAll("§[0-9a-fk-or]", "");
                    if (!clean.isEmpty()) tempMembers.add(clean);
                }
            } else if (!line.isEmpty()) {
                // Einzelner Name, der auf die neue Zeile gerutscht ist
                String clean = line.trim().replaceAll("§[0-9a-fk-or]", "");
                if (!clean.isEmpty()) tempMembers.add(clean);
            }
        }
    }

    private void finishCapturing() {
        if (!capturingMembers) return;
        capturingMembers = false;

        boolean isAlliance = false;
        if (addon.configuration().factionMemberColor().enableAllianceMemberColor().get()
            && addon.configuration().factionMemberColor().allianceFaction().get() != FactionType.ZIVILIST) {
            String allianceFolderName = addon.configuration().factionMemberColor().allianceFaction().get().getName();
            if (capturingFactionName.equalsIgnoreCase(allianceFolderName)) {
                isAlliance = true;
            }
        }

        // Speicher in eigener Datei persistent abspeichern
        if (isAlliance) {
            de.asxka.core.utils.AllianceCache.save(tempMembers);
            if (isAutoSync) {
                de.asxka.core.utils.NotificationUtils.pushComponentNotification(
                    GradientUtils.gradient("ᴜɴɪᴄᴀᴄɪᴛʏ ᴇѕѕᴇɴᴛɪᴀʟѕ", TextColor.color(0xa17cf7), TextColor.color(0x9337c4)),
                    Component.text("Bündnismitglieder erfolgreich synchronisiert! ", NamedTextColor.GRAY)
                             .append(Component.text("(" + tempMembers.size() + " Mitglieder)", NamedTextColor.GREEN))
                );
            }
        } else {
            FactionCache.save(tempMembers);
            if (isAutoSync) {
                de.asxka.core.utils.NotificationUtils.pushComponentNotification(
                    GradientUtils.gradient("ᴜɴɪᴄᴀᴄɪᴛʏ ᴇѕѕᴇɴᴛɪᴀʟѕ", TextColor.color(0xa17cf7), TextColor.color(0x9337c4)),
                    Component.text("Fraktionsmitglieder erfolgreich synchronisiert! ", NamedTextColor.GRAY)
                             .append(Component.text("(" + tempMembers.size() + " Mitglieder)", NamedTextColor.GREEN))
                );
            }
        }

        tempMembers.clear();
        capturingFactionName = "";
        isAutoSync = false;
    }
}
