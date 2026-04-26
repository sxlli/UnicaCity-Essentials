package de.asxka.core.unused;

import de.asxka.core.UnicaCityEssentials;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;

public class ChestRNGWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TextLine chestRNGLine;
  private final UnicaCityEssentials addon;

  public ChestRNGWidget(String id, UnicaCityEssentials addon) {
    super(id);
    this.addon = addon;
  }

  @Override
  public void load (TextHudWidgetConfig config) {
    super.load(config);

    String savedChestRNG = this.addon.configuration().savedChestRNG().get();
    this.chestRNGLine = super.createLine("Kisten Pity");
  }

  @Override
  public boolean isVisibleInGame() {
    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player == null) return false;

    ItemStack mainHand = player.getMainHandItemStack();
    ItemStack offHand = player.getOffHandItemStack();

    boolean holdingRod = false;
    if (mainHand != null && !mainHand.isAir()) {
      holdingRod |= mainHand.getAsItem().getIdentifier().getPath().contains("fishing");
    }
    if (offHand != null && !offHand.isAir()) {
      holdingRod |= offHand.getAsItem().getIdentifier().getPath().contains("fishing");
    }

    return super.isVisibleInGame() && holdingRod;
  }

  @Subscribe
  public void onTitle(TitleReceiveEvent event) {
    String titleText = event.getTitle();
    if (titleText == null) return;

    // Beispiel Logic wie im FishingWidget:
    // Hier den Regex anpassen, falls der Title anders aussieht!
    // z.B. wenn der Title ein Text wie "Kisten Pity: 50" ist
    if (titleText.contains("Kisten Pity") || titleText.contains("Pity")) {
      // Extrahiere die Pity / den Wert und speichere ihn
      // this.addon.configuration().savedChestRNG().set(neuerWert);
      // this.chestRNGLine.updateAndFlush(neuerWert);
      this.addon.configuration().savedChestRNG().set(titleText);
      this.chestRNGLine.updateAndFlush(titleText);
    }
  }
}
