package de.asxka.core.listener;

import de.asxka.core.utils.GradientUtils;
import de.asxka.core.utils.PatternUtils;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.notification.Notification;
import de.asxka.core.UnicaCityEssentials;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FriendNotificationListener {

  private final UnicaCityEssentials addon;
  public PatternUtils patternUtils = new PatternUtils();

  public FriendNotificationListener(UnicaCityEssentials addon) {
    this.addon = addon;
  }


  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    String message = event.chatMessage().getPlainText();
    if (message == null) return;

    Matcher matcher = patternUtils.friendStatusPattern.matcher(message);
    if (matcher.find()) {
      // Wenn die Config-Einstellung deaktiviert ist, machen wir gar nichts (Chatnachricht bleibt normal sichtbar)
      if (!this.addon.configuration().FriendlistNotify().get()) {
        return;
      }

      // Blendet die eigentliche Nachricht im Chat aus!
      event.setCancelled(true);

      String rawPlayerName = matcher.group(1).trim();
      String status = matcher.group(2);      // z.B. "Online" oder "Offline"

      // Entfernt Clan-Tags wie [UC] und unsichtbare Sonderzeichen, um den reinen Minecraft-Namen für den Skin zu ermitteln
      String headName = rawPlayerName.replaceAll("\\[.*?\\]", "").replaceAll("[^a-zA-Z0-9_]", "");

      // Je nach Status färben wir den Text in der Notification etwas anders
      boolean isOnline = status.equalsIgnoreCase("Online");
      TextColor statusColor = isOnline ? NamedTextColor.GREEN : NamedTextColor.RED;

      Notification.Builder builder = Notification.builder()
          .title(GradientUtils.gradient("ꜰʀᴇᴜɴᴅᴇѕʟɪѕᴛᴇ", TextColor.color(0x2bed21), TextColor.color(0x168211)))
          .text(
              Component.text(rawPlayerName + " ", NamedTextColor.GRAY)
                  .append(Component.text("ist nun ", NamedTextColor.GRAY))
                  .append(Component.text(status, statusColor))
          )
          .icon(Icon.head(headName)) // Hier wird nun der gesäuberte Name abgerufen
          .duration(isOnline ? 7500 : 7500); // 4 Sekunden wenn online, 3 wenn offline

      builder.buildAndPush();
    }
  }
}
