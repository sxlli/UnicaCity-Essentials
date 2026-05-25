package de.asxka.core.widgets;

import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.world.effect.PotionEffect;
import java.util.Collection;

public class AbsorptionWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TextLine absorptionLine;
  private int currentDurationTicks = 0;
  private boolean hadEffectLastTick = false;

  private final int FIXED_COOLDOWN_TICKS = 1800;

  public AbsorptionWidget(String id) {
    super(id);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.absorptionLine = super.createLine("Absorption", "01:30");
  }

  @Override
  public boolean isVisibleInGame() {
    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player == null) return false;

    net.labymod.api.client.world.item.ItemStack mainHand = player.getMainHandItemStack();

    if (mainHand != null && !mainHand.isAir()) {
      boolean matches = false;
      try {
        for (java.lang.reflect.Method m : mainHand.getClass().getMethods()) {
            if (m.getParameterCount() != 0) continue;
            String methodName = m.getName().toLowerCase();
            if (!methodName.equals("getname") && !methodName.equals("gethovername") && !methodName.equals("getdisplayname") && !methodName.equals("getcustomname")) continue;

            Object val = m.invoke(mainHand);
            if (val != null && val.toString().toLowerCase().contains("ts19")) {
                matches = true;
                break;
            }
        }
      } catch (Exception ignored) {}

      String itemName = mainHand.getAsItem().getIdentifier().getPath().toLowerCase();
      return matches || itemName.contains("ts19") || itemName.contains("ts-19") || itemName.contains("ts_19");
    }

    return false;
  }

  @Override
  public void onTick(boolean isEditorContext) {
    if (isEditorContext) return;

    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player != null) {
      Collection<PotionEffect> effects = player.getActivePotionEffects();
      boolean hasAbsorptionNow = false;

      if (effects != null) {
        for (PotionEffect effect : effects) {
          if (effect.getTranslationKey().toLowerCase().contains("absorption")) {
            hasAbsorptionNow = true;
            break;
          }
        }
      }

      if (hasAbsorptionNow && !hadEffectLastTick) {
        currentDurationTicks = FIXED_COOLDOWN_TICKS;
      }

      hadEffectLastTick = hasAbsorptionNow;

      if (currentDurationTicks > 0) {
        currentDurationTicks--;

        int seconds = (int) Math.ceil(currentDurationTicks / 20.0);
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        this.absorptionLine.updateAndFlush(String.format("%02d:%02d", minutes, remainingSeconds));
        this.absorptionLine.setVisible(true);
      } else {
        this.absorptionLine.setVisible(false);
      }
    }
  }
}