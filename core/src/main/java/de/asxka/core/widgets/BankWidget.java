package de.asxka.core.widgets;

import de.asxka.core.UnicaCityEssentials;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;

/**
 * Simple HUD widget showing bank and cash balances. The GUI was removed by request,
 * but the widget should remain to display stored bank info.
 */
public class BankWidget extends TextHudWidget<TextHudWidgetConfig> {

  private final UnicaCityEssentials addon;

  private TextLine bankLine;
  private TextLine cashLine;

  private long bankValue;
  private long cashValue;

  public BankWidget(String id, UnicaCityEssentials addon) {
    super(id);
    this.addon = addon;
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.bankValue = parseStoredAmount(this.addon.configuration().savedBankBalance().get());
    this.cashValue = parseStoredAmount(this.addon.configuration().savedMoneyBalance().get());
    this.bankLine = super.createLine("Bank", formatAmount(this.bankValue));
    this.cashLine = super.createLine("Bargeld", formatAmount(this.cashValue));
    refresh();
  }

  public void setBankValue(long value) {
    this.bankValue = Math.max(0L, value);
    this.addon.configuration().savedBankBalance().set(Long.toString(this.bankValue));
    refresh();
  }

  public void addBankValue(long delta) {
    setBankValue(this.bankValue + delta);
  }

  public void setCashValue(long value) {
    this.cashValue = Math.max(0L, value);
    this.addon.configuration().savedMoneyBalance().set(Long.toString(this.cashValue));
    refresh();
  }

  public void addCashValue(long delta) {
    setCashValue(this.cashValue + delta);
  }

  @Override
  public void onTick(boolean isEditorContext) {
    if (isEditorContext) {
      this.bankLine.updateAndFlush("Bank: 1.234.567$");
      this.cashLine.updateAndFlush("Bargeld: 12.345$");
      return;
    }

    refresh();
  }

  private void refresh() {
    if (this.bankLine != null) {
      this.bankLine.updateAndFlush(formatAmount(this.bankValue));
    }
    if (this.cashLine != null) {
      this.cashLine.updateAndFlush(formatAmount(this.cashValue));
    }
  }

  private String formatAmount(long value) {
    return String.format(java.util.Locale.GERMANY, "%,d$", value).replace(',', '.');
  }

  private long parseStoredAmount(String value) {
    if (value == null || value.trim().isEmpty()) {
      return 0L;
    }

    try {
      return Long.parseLong(value.replace(".", "").replace(",", "").replace("$", "").trim());
    } catch (Exception ignored) {
      return 0L;
    }
  }
}




