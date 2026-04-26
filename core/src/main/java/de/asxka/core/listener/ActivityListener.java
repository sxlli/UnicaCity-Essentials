package de.asxka.core.listener;

import de.asxka.core.utils.PatternUtils;
import de.asxka.core.widgets.ActivityWidget;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

public class ActivityListener {

  private final ActivityWidget activityWidget;
  private final PatternUtils patternUtils;

  public ActivityListener(ActivityWidget activityWidget, PatternUtils patternUtils) {
    this.activityWidget = activityWidget;
    this.patternUtils = patternUtils;
  }

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    String message = event.chatMessage().getPlainText();

    // Bombe
    if (this.patternUtils.bombplacePattern.matcher(message).find()) {
      this.activityWidget.startBombTimer(); 
      return;
    }
    if (this.patternUtils.bombdefusePattern.matcher(message).find() ||
        this.patternUtils.bombexplodePattern.matcher(message).find()) {
      this.activityWidget.stopBombTimer();
      return;
    }

    // Staatsbank
    if (this.patternUtils.bankrobberyStartPattern.matcher(message).find() && !this.patternUtils.bankrobberyStartPattern.pattern().isEmpty()) {
      this.activityWidget.startBankTimer();
      return;
    }
    if ((this.patternUtils.bankrobberyEndPattern.matcher(message).find() && !this.patternUtils.bankrobberyEndPattern.pattern().isEmpty()) ||
        (this.patternUtils.bankrobberyFailedPattern.matcher(message).find() && !this.patternUtils.bankrobberyFailedPattern.pattern().isEmpty())) {
      this.activityWidget.stopBankTimer();
      return;
    }
  }
}
