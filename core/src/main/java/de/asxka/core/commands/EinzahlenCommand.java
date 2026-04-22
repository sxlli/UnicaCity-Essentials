package de.asxka.core.commands;

import de.asxka.core.SolaraAddon;
import de.asxka.core.utils.GradientUtils;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;

public class EinzahlenCommand extends Command {

  private final SolaraAddon addon;

  public EinzahlenCommand(SolaraAddon addon) {
    super("einzahlen", "bankeinzahlen");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    // Ruft den letzten im BankWidget gemerkten Bargeld-Kontostand ab
    String money = this.addon.configuration().savedMoneyBalance().get();

    // Überprüfen, ob überhaupt ein Wert erkannt wurde und er nicht leer ist
    if (money != null && !money.isEmpty() && !money.equals("0")) {
      // Sendet den Command "/bank einzahlen <Geld>" ab
      this.sendMessage("/bank einzahlen " + money);
      return true;
    }

    // Falls bisher kein Geld im Chat gelesen wurde, melde dies dem Spieler (optional)
    this.displayMessage(
        GradientUtils.gradient(" ᴜᴄᴇ ", TextColor.color(0x6a43e8), TextColor.color(0x405cd6))
            .append(Component.text("» ", NamedTextColor.DARK_GRAY))
            .append(Component.text("ᴅᴜ ʜᴀѕᴛ ᴍᴏᴍᴇɴᴛᴀɴ ᴋᴇɪɴ ɢᴇʟᴅ ᴀᴜꜱ ᴅᴇʀ ʜᴀɴᴅ.", NamedTextColor.RED))
    );
    return true; // <- true (statt false), damit der Command NICHT an den Server weitergeleitet wird!
  }
}
