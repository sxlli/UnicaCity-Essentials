package de.asxka.core.widgets;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import de.asxka.core.SolaraAddon;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class BankWidget extends TextHudWidget<TextHudWidgetConfig> {

  // Erste Nachricht fürs Bankguthaben (mit . statt ä, um mögliche Encoding-Probleme zu verhindern)
  private final Pattern bankPattern = Pattern.compile("Bankguthaben betr.gt:\\s*[+-]?(\\d+)");
  // Zweite Möglichkeit fürs Bankguthaben (Beispiel)
  private final Pattern bankUpdatePattern = Pattern.compile("Neuer Kontostand:\\s*[+-]?(\\d+)");

  // Nachrichten für Ein- und Auszahlung (um das Bargeld zu berechnen)
  private final Pattern depositPattern = Pattern.compile("Eingezahlt:\\s*\\+(\\d+)");
  private final Pattern withdrawPattern = Pattern.compile("Auszahlung:\\s*-(\\d+)");

  // Nachricht fürs Bargeld
  private final Pattern moneyPattern = Pattern.compile("Geld:\\s*[+-]?(\\d+)"); //

  private TextLine bankLine;
  private TextLine moneyLine;
  private final SolaraAddon addon;

  public BankWidget(String id, SolaraAddon addon) {
    super(id);
    this.addon = addon;
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);

    // Initialisiere die Zeile mit den gespeicherten Werten
    String savedBank = this.addon.configuration().savedBankBalance().get();
    String savedMoney = this.addon.configuration().savedMoneyBalance().get();

    this.bankLine = super.createLine("Bank", savedBank + "$");
    this.moneyLine = super.createLine("Geld", savedMoney + "$");
  }

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    String message = event.chatMessage().getPlainText();

    Matcher bankMatcher = bankPattern.matcher(message);
    Matcher bankUpdateMatcher = bankUpdatePattern.matcher(message);
    Matcher moneyMatcher = moneyPattern.matcher(message);
    Matcher depositMatcher = depositPattern.matcher(message);
    Matcher withdrawMatcher = withdrawPattern.matcher(message);

    // Bank-Guthaben aktualisieren (if / else if gekoppelt, da beides die Bank betrifft)
    if (bankMatcher.find()) {
      String value = bankMatcher.group(1);
      this.addon.configuration().savedBankBalance().set(value);
      this.bankLine.updateAndFlush(value + "$");
    } else if (bankUpdateMatcher.find()) {
      String value = bankUpdateMatcher.group(1);
      this.addon.configuration().savedBankBalance().set(value);
      this.bankLine.updateAndFlush(value + "$");
    }

    // Bargeld aktualisieren (in einem eigenen Block, falls Server die Nachrichten kombiniert schickt)
    if (moneyMatcher.find()) {
      String value = moneyMatcher.group(1);
      this.addon.configuration().savedMoneyBalance().set(value);
      this.moneyLine.updateAndFlush(value + "$");
    } else if (depositMatcher.find()) {
      // Geld eingezahlt -> Bargeld abziehen
      int change = Integer.parseInt(depositMatcher.group(1));
      int currentMoney = 0;
      try {
        currentMoney = Integer.parseInt(this.addon.configuration().savedMoneyBalance().get());
      } catch(Exception ignored){}

      int newMoney = Math.max(0, currentMoney - change); // nicht unter 0 gehen
      this.addon.configuration().savedMoneyBalance().set(String.valueOf(newMoney));
      this.moneyLine.updateAndFlush(newMoney + "$");
    } else if (withdrawMatcher.find()) {
      // Geld ausgezahlt -> Bargeld aufstocken
      int change = Integer.parseInt(withdrawMatcher.group(1));
      int currentMoney = 0;
      try {
        currentMoney = Integer.parseInt(this.addon.configuration().savedMoneyBalance().get());
      } catch(Exception ignored){}

      int newMoney = currentMoney + change;
      this.addon.configuration().savedMoneyBalance().set(String.valueOf(newMoney));
      this.moneyLine.updateAndFlush(newMoney + "$");
    }
  }
}
