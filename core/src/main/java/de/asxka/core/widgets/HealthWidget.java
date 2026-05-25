package de.asxka.core.widgets;

import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.world.item.ItemStack;

public class HealthWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TextLine healthLine;

  public HealthWidget(String id) {
    super(id);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);

    this.healthLine = super.createLine("❤", "10.0");
  }


  @Override
  public void onTick(boolean isEditorContext) {
    if (isEditorContext) {
      this.healthLine.updateAndFlush("10.0 + 2.0");
      return;
    }

    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player == null) {
      return;
    }

    float health = 0;
    float absorption = 0;

    for (java.lang.reflect.Method m : player.getClass().getMethods()) {
      if (m.getParameterCount() != 0) continue;
      String name = m.getName().toLowerCase();
      try {
        if (name.equals("gethealth") || name.equals("health")) {
          health = ((Number) m.invoke(player)).floatValue();
        } else if (name.equals("getabsorptionamount") || name.equals("absorptionamount") || name.equals("absorption")) {
          absorption = ((Number) m.invoke(player)).floatValue();
        }
      } catch (Exception ignored) {}
    }

    float healthHearts = (float) Math.ceil(health) / 2.0f;
    float absosHearts = (float) Math.ceil(absorption) / 2.0f;

    String value;
    if (absosHearts > 0) {
      value = String.format("%.1f + %.1f", healthHearts, absosHearts);
    } else {
      value = String.format("%.1f", healthHearts);
    }

    this.healthLine.updateAndFlush(value);
  }
}
