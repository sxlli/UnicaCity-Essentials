package de.asxka.core.widgets;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.event.Subscribe;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.serializer.plain.PlainTextComponentSerializer;
import net.labymod.api.event.client.world.ItemStackTooltipEvent;
import de.asxka.core.UnicaCityEssentials;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.client.chat.ActionBarReceiveEvent;
import net.labymod.api.event.Phase;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FishingWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TextLine fishingline;
  private final UnicaCityEssentials addon;
  private final Pattern talentExpPattern = Pattern.compile("\\[Talent\\] \\+([0-99]+) EXP für.*?(?:Angeln|ANGELN|ᴀɴɢᴇʟɴ)");

  public FishingWidget(String id, UnicaCityEssentials addon) {
    super(id);
    this.addon = addon;
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);

    // Initialisiere die Zeile
    String savedExp = this.addon.configuration().savedFishingExp().get();
    this.fishingline = super.createLine("Fishing EXP", savedExp);
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
  public void onActionBar(ActionBarReceiveEvent event) {
    if (event.phase() != Phase.PRE) return;
    if (event.getMessage() == null) return;

    String text = PlainTextComponentSerializer.plainText().serialize(event.getMessage()).trim();
    Matcher m = talentExpPattern.matcher(text);
    if (m.find()) {
      int added = Integer.parseInt(m.group(1));
      String currentExpStr = this.addon.configuration().savedFishingExp().get();
      if (currentExpStr != null && currentExpStr.contains("/")) {
        String[] parts = currentExpStr.split("/", 2);
        try {
          int current = Integer.parseInt(parts[0].trim());
          String max = parts[1].trim();
          String newExpStr = (current + added) + " / " + max;
          this.addon.configuration().savedFishingExp().set(newExpStr);
          this.fishingline.updateAndFlush(newExpStr);
        } catch (NumberFormatException ignored) {}
      }
    }
  }

  @Subscribe
  public void onTooltip(ItemStackTooltipEvent event) {
    if (event.itemStack() == null) return;

    List<Component> lines = event.getTooltipLines();
    if (lines.isEmpty()) return;

    // Prüfen, ob eine beliebige Zeile im Tooltip das Wort "Angeln" (oder die spezielle Schriftart) enthält.
    boolean isFishingItem = false;
    for (Component comp : lines) {
      String text = PlainTextComponentSerializer.plainText().serialize(comp).trim();
      if (text.contains("ANGELN") || text.contains("Angeln") || text.contains("ᴀɴɢᴇʟɴ")) {
        isFishingItem = true;
        break;
      }
    }

    if (isFishingItem) {
      for (Component comp : lines) {
        String text = PlainTextComponentSerializer.plainText().serialize(comp).trim();

        // Da in der Lore davor anscheinend ein Pfeil (z.B. "-> EXP: 245 / 1000") steht,
        // nutzen wir contains statt startsWith
        if (text.contains("EXP:")) {
          int idx = text.indexOf("EXP:");
          String expPart = text.substring(idx + 4).trim(); // Schneidet alles vor "EXP:" ab und das "EXP:" selbst

          this.addon.configuration().savedFishingExp().set(expPart);
          this.fishingline.updateAndFlush(expPart);
        }
      }
    }
  }
}
