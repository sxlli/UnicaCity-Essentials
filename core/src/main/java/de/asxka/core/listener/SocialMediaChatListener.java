package de.asxka.core.listener;

import de.asxka.core.utils.GradientUtils;
import de.asxka.core.utils.PatternUtils;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocialMediaChatListener {

  public PatternUtils patternUtils = new PatternUtils();

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    String plainMessage = event.chatMessage().getPlainText();
    Matcher matcher = patternUtils.TEAM_SocialMedia_Chat.matcher(plainMessage);

    if (matcher.find()) {
      String playerName = matcher.group(1);
      String messageContent = matcher.group(2);

    Component new_sm_prefix = GradientUtils.gradient("ѕᴏᴄɪᴀʟ ᴍᴇᴅɪᴀ", TextColor.color(0x8735d4), TextColor.color(0x4a35d4))
            .append(Component.text(" x ", NamedTextColor.DARK_GRAY))
            .append(GradientUtils.gradient(playerName, TextColor.color(0x4a35d4), TextColor.color(0x3575d4))) // <--- Hier kannst du den Namen extra stylen
            .append(Component.text(" " + messageContent, NamedTextColor.GRAY));

      event.setMessage(new_sm_prefix);
    }
  }
}
