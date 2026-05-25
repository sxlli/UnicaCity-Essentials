package de.asxka.core.listener;

import de.asxka.core.UnicaCityEssentials;
import de.asxka.core.utils.PatternUtils;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

import java.util.regex.Matcher;

public class BankInfoListener {

  private final UnicaCityEssentials addon;
  private final PatternUtils patternUtils = new PatternUtils();
  private volatile long ignoreCashDeltaUntil = 0L;
  private volatile long statsModeUntil = 0L;
  private volatile long accountStatementModeUntil = 0L;

  public BankInfoListener(UnicaCityEssentials addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    String msg = event.chatMessage().getPlainText();
    if (msg.trim().isEmpty()) return;

    msg = msg.replace("[.]", "").replace("📋", "").trim();

    try {
      Matcher m;

      boolean isPaydayMessage = msg.contains("PayDay") || msg.contains("Payday") || msg.contains("Neuer Betrag:");
      boolean isStatsMessage = patternUtils.statsPattern.matcher(msg).find();
      boolean isAccountStatementMessage = patternUtils.accountStatementPattern.matcher(msg).find();
      boolean isFactionBankMessage = msg.contains("Fraktionsbank") || msg.contains("F-Bank") || msg.contains("ꜰ-ʙᴀɴᴋ") || msg.contains("F-Bank]");

      if (isAccountStatementMessage) {
        this.accountStatementModeUntil = System.currentTimeMillis() + 3000L;
      }

      if (isFactionBankMessage) {
        return;
      }

      if (System.currentTimeMillis() < this.statsModeUntil) {
        m = patternUtils.moneyPattern.matcher(msg);
        if (m.find()) {
          this.addon.bankWidget().setCashValue(parseAmount(m.group(1)));
          return;
        }
      }

      if (isStatsMessage) {
        this.statsModeUntil = System.currentTimeMillis() + 3000L;
        m = patternUtils.moneyPattern.matcher(msg);
        if (m.find()) {
          this.addon.bankWidget().setCashValue(parseAmount(m.group(1)));
          this.ignoreCashDeltaUntil = System.currentTimeMillis() + 700L;
        }
        return;
      }

      m = patternUtils.paydayNewAmountPattern.matcher(msg);
      if (m.find()) {
        this.addon.bankWidget().setBankValue(parseAmount(m.group(1)));
        return;
      }

      m = patternUtils.bankPattern.matcher(msg);
      if (m.find()) {
        this.addon.bankWidget().setBankValue(parseAmount(m.group(1)));
        return;
      }

      m = patternUtils.bankUpdatePattern.matcher(msg);
      boolean hasBankUpdate = false;
      if (m.find()) {
        this.addon.bankWidget().setBankValue(parseAmount(m.group(1)));
        hasBankUpdate = true;
      }

      boolean inAccountStatementMode = System.currentTimeMillis() < this.accountStatementModeUntil;

      if (!isPaydayMessage && !inAccountStatementMode && !hasBankUpdate) {
        if (System.currentTimeMillis() < this.statsModeUntil || System.currentTimeMillis() < this.ignoreCashDeltaUntil) {

        } else {
          long delta = extractSignedDelta(msg);
          if (delta != Long.MIN_VALUE) {
            this.addon.bankWidget().addCashValue(delta);
            return;
          }
        }
      }

      m = patternUtils.depositPattern.matcher(msg);
      if (m.find()) {
        long amount = Math.abs(parseAmount(m.group(1)));
        this.addon.bankWidget().addCashValue(-amount);
        if (!hasBankUpdate) {
            this.addon.bankWidget().addBankValue(amount);
        }
        return;
      }

      m = patternUtils.withdrawPattern.matcher(msg);
      if (m.find()) {
        long amount = Math.abs(parseAmount(m.group(1)));
        this.addon.bankWidget().addCashValue(amount);
        if (!hasBankUpdate) {
            this.addon.bankWidget().addBankValue(-amount);
        }
        return;
      }

      if (!isPaydayMessage && System.currentTimeMillis() >= this.statsModeUntil && !inAccountStatementMode) {
        m = patternUtils.moneyPattern.matcher(msg);
        if (m.find()) {
          this.addon.bankWidget().setCashValue(parseAmount(m.group(1)));
        }
      }

    } catch (Exception ignored) {
    }
  }

  private long parseAmount(String amount) {
    try {
      return Long.parseLong(amount.replace(".", "").replace(",", ""));
    } catch (Exception e) {
      return 0L;
    }
  }

  private long extractSignedDelta(String msg) {
    Matcher m = patternUtils.moneyChangePattern.matcher(msg);
    long delta = Long.MIN_VALUE;
    while (m.find()) {
      long amount = parseAmount(m.group(2));
      String sign = m.group(1);
      delta = ("-".equals(sign) || "−".equals(sign)) ? -amount : amount;
    }
    return delta;
  }
}
