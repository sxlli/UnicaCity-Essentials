package de.asxka.core.widgets;

import de.asxka.core.utils.PatternUtils;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;

public class ActivityWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TextLine bombLine;
  private TextLine bankLine;

  private long bombStartTime = 0;
  private boolean bombActive = false;

  private long bankStartTime = 0;
  private boolean bankActive = false;

  public ActivityWidget(String id) {
    super(id);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);

    this.bombLine = super.createLine("Bombe: ", "");
    this.bankLine = super.createLine("Staatsbank: ", "");
  }

  public void startBombTimer() {
    this.bombStartTime = System.currentTimeMillis();
    this.bombActive = true;
  }

  public void stopBombTimer() {
    this.bombActive = false;
    this.bombStartTime = 0;
    this.bombLine.updateAndFlush("");
  }

  public void startBankTimer() {
    this.bankStartTime = System.currentTimeMillis();
    this.bankActive = true;
  }

  public void stopBankTimer() {
    this.bankActive = false;
    this.bankStartTime = 0;
    this.bankLine.updateAndFlush("");
  }

  public void onTick(boolean isEditorContext) {
    if (isEditorContext) {
      this.bombLine.updateAndFlush("13m 42s");
      this.bankLine.updateAndFlush("30m 10s");
      this.bombLine.setVisible(true);
      this.bankLine.setVisible(true);
      return;
    }

    if (this.bombActive) {
      long elapsed = System.currentTimeMillis() - this.bombStartTime;
      long minutes = (elapsed / 1000) / 60;
      long seconds = (elapsed / 1000) % 60;
      String timeString = String.format("%dm %ds", minutes, seconds);
      if (minutes >= 13) timeString = "§c" + timeString;
      this.bombLine.updateAndFlush(timeString);
      this.bombLine.setVisible(true);
    } else {
      this.bombLine.setVisible(false);
    }

    if (this.bankActive) {
      long elapsed = System.currentTimeMillis() - this.bankStartTime;
      long minutes = (elapsed / 1000) / 60;
      long seconds = (elapsed / 1000) % 60;
      String timeString = String.format("%dm %ds", minutes, seconds);
      if (minutes >= 30) timeString = "§c" + timeString;
      this.bankLine.updateAndFlush(timeString);
      this.bankLine.setVisible(true);
    } else {
      this.bankLine.setVisible(false);
    }
  }
}
