package de.asxka.core.widgets;

import de.asxka.core.utils.PatternUtils;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.chat.ChatMessageSendEvent;
import java.util.regex.Matcher;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PayDayWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TextLine paydayLine;
  public PatternUtils patternUtils = new PatternUtils();

  private int currentMinutes = 0;
  private long lastTickTime = 0;
  private int tickCounter = 0;
  private boolean isAfk = false;

  private final Path cacheFile = Paths.get("unicacity_payday.txt");

  public PayDayWidget(String id) {
    super(id);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    try {
      if (Files.exists(cacheFile)) {
        currentMinutes = Integer.parseInt(Files.readString(cacheFile).trim());
      }
    } catch (Exception ignored) {}
    this.paydayLine = super.createLine("PayDay", currentMinutes + "/60");
  }

  private void savePayday() {
    try {
      Files.writeString(cacheFile, String.valueOf(currentMinutes));
    } catch (Exception ignored) {}
  }

  @Override
  public void onTick(boolean isEditorContext) {
    if (isEditorContext) return;

    if (!isAfk) {
      tickCounter++;
      // 20 ticks = 1 second, 1200 ticks = 60 seconds (1 minute)
      if (tickCounter >= 1200) {
        tickCounter = 0;
        currentMinutes++;
        if (currentMinutes > 60) {
          currentMinutes = 0; // Reset after reaching 60, adjusting if needed
        }
        updatePaydayLine();
        savePayday();
      }
    }
  }

  private void updatePaydayLine() {
    this.paydayLine.updateAndFlush(currentMinutes + "/60");
  }

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    String message = event.chatMessage().getPlainText();

    if (message.toLowerCase().contains("======== payday ========")) {
      currentMinutes = 0;
      tickCounter = 0;
      updatePaydayLine();
      savePayday();
    }

    Matcher paydayTime = patternUtils.paydayTimer.matcher(message);

    if (paydayTime.find()) {
      String time = paydayTime.group(1);
      try {
        currentMinutes = Integer.parseInt(time);
        tickCounter = 0; // reset ticks to align perfectly with /stats updating it
        updatePaydayLine();
        savePayday();
      } catch (NumberFormatException ignored) {}
    }

    // Generic match for UnicaCity afk messages (usually "Du bist nun AFK" / "Du bist nun wieder anwesend")
    if (message.contains("Du bist nun im AFK-Modus.")) {
      isAfk = true;
    } else if (message.contains("Du bist nun nicht mehr im AFK-Modus.") || message.contains("Willkommen zurück!")) {
      isAfk = false;
    }
  }

  @Subscribe
  public void onChatMessageSend(ChatMessageSendEvent event) {
    String msg = event.getMessage().toLowerCase();
    if (msg.equals("/afk")) {
      // Toggle AFK as fallback (if the server messages are different)
      isAfk = !isAfk;
    }
  }
}
