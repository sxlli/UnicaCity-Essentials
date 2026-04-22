package de.asxka.core.listener.Faction;

import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.chat.ChatMessageSendEvent;
import net.labymod.api.event.client.render.PlayerNameTagRenderEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WantedListener {

  // Static Map um sicherzustellen, dass die Reconnects im gleichen Game-Start überlebt werden.
  private static final Map<String, Integer> wantedLevels = new ConcurrentHashMap<>();

  // - [UC]Maagma46 ¦ 69 WPS (Massenmord) [.]
  // - TanerMonos ¦ 65 WPS (Pfandnahme) [.]
  private final Pattern wantedListPattern = Pattern.compile("^\\s*-\\s*(?:\\[.*?\\])?([a-zA-Z0-9_]+)\\s*[|¦]\\s*(\\d+)\\s*WPS");

  // NAME´s momentanes WantedLevel: WPSanzahl
  private final Pattern wantedLevelPattern = Pattern.compile("(?:\\[.*?\\])?([a-zA-Z0-9_]+)['´]s momentanes WantedLevel: (\\d+)");

  // hat NAME´s Akten gelöscht
  private final Pattern wantedClearedPattern = Pattern.compile("hat (?:\\[.*?\\])?([a-zA-Z0-9_]+)['´]s Akten gel[öo]scht");

  // NAME wurde von JEMAND getötet
  private final Pattern killedPattern = Pattern.compile("(?:\\[.*?\\])?([a-zA-Z0-9_]+) wurde von .*? get[öo]tet");

  // NAME wurde von JEMAND eingesperrt
  private final Pattern jailedPattern = Pattern.compile("(?:\\[.*?\\])?([a-zA-Z0-9_]+) wurde von .*? eingesperrt");

  private long lastWantedsCommandTime = 0;

  @Subscribe
  public void onChatMessageSend(ChatMessageSendEvent event) {
    String msg = event.getMessage().toLowerCase();
    if (msg.equals("/wanteds")) {
      // Clear the map if we request a new list
      this.wantedLevels.clear();
      this.lastWantedsCommandTime = System.currentTimeMillis();
    }
  }

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    String message = event.chatMessage().getPlainText();
    if (message == null) return;
    
    // Clear wanteds if we receive the header (so it empties when no one has wanteds, or resets before parsing the new list)
    if (message.contains("Online Spieler mit WantedPunkten:")) {
      wantedLevels.clear();
    }

    // Check multiline for wanteds
    if (message.contains("\n")) {
      String[] lines = message.split("\n");
      for (String line : lines) {
        checkAndAddWanted(line.trim());
      }
    } else {
      checkAndAddWanted(message.trim());
    }

    // 2. Check for "momentanes WantedLevel: X"
    Matcher levelMatcher = wantedLevelPattern.matcher(message.trim());
    if (levelMatcher.find()) {
      String name = levelMatcher.group(1);
      int wps = Integer.parseInt(levelMatcher.group(2));
      wantedLevels.merge(name, wps, Math::max);
      return;
    }

    // 3. Check for cleared wanteds
    Matcher clearedMatcher = wantedClearedPattern.matcher(message);
    if (clearedMatcher.find()) {
      String name = clearedMatcher.group(1);
      wantedLevels.remove(name);
    }

    // 4. Check for killed
    Matcher killedMatcher = killedPattern.matcher(message);
    if (killedMatcher.find()) {
      String name = killedMatcher.group(1);
      wantedLevels.remove(name);
    }

    // 5. Check for jailed
    Matcher jailedMatcher = jailedPattern.matcher(message);
    if (jailedMatcher.find()) {
      String name = jailedMatcher.group(1);
      wantedLevels.remove(name);
    }
  }

  private void checkAndAddWanted(String line) {
    Matcher listMatcher = wantedListPattern.matcher(line);
    if (listMatcher.find()) {
      String name = listMatcher.group(1);
      int wps = Integer.parseInt(listMatcher.group(2));
      wantedLevels.merge(name, wps, Math::max);
    }
  }

  @Subscribe
  public void onPlayerNameTagRender(PlayerNameTagRenderEvent event) {
    if (event.context() != PlayerNameTagRenderEvent.Context.PLAYER_RENDER) return;
    if (event.getPlayerInfo() == null || event.getPlayerInfo().profile() == null) return;
    String name = event.getPlayerInfo().profile().getUsername();

    if (wantedLevels.containsKey(name)) {
      int wps = wantedLevels.get(name);
      event.setNameTag(event.nameTag().color(getColorForWPS(wps)));
    }
  }

  private TextColor getColorForWPS(int wps) {
    if (wps == 1) return NamedTextColor.DARK_GREEN;
    if (wps >= 2 && wps <= 10) return NamedTextColor.GREEN;
    if (wps >= 11 && wps <= 30) return NamedTextColor.GOLD;
    if (wps >= 31 && wps <= 49) return NamedTextColor.YELLOW;
    if (wps >= 50 && wps <= 59) return NamedTextColor.RED;
    return NamedTextColor.DARK_RED; // 60+ WPS
  }
}
