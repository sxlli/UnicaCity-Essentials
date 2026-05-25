package de.asxka.core.widgets;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CarLockWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TextLine carLine;

  public CarLockWidget(String id) {
    super(id);
  }

  private boolean isLocked = true;
  private String carName = "N/A";
  private final Path cacheFile = Paths.get("uce_car.txt");

  public void setLocked(boolean locked, String carName) {
    this.isLocked = locked;
    this.carName = carName;
    updateLine();
  }

  private void updateLine() {
    if (this.carLine != null) {
      this.carLine.updateAndFlush(isLocked ? "§czu" : "§aauf");
      savePayday();
    }
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.carLine = this.createLine("Auto", isLocked ? "§czu" : "§aauf");
  }

  private void savePayday() {
    try {
      Files.writeString(cacheFile, String.valueOf(isLocked));
    } catch (Exception ignored) {}
  }

}
