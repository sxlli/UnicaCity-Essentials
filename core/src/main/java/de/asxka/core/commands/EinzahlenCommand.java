package de.asxka.core.commands;

import de.asxka.core.UnicaCityEssentials;
import de.asxka.core.utils.GradientUtils;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;

public class EinzahlenCommand extends Command {

  private final UnicaCityEssentials addon;

  public EinzahlenCommand(UnicaCityEssentials addon) {
    super("einzahlen", "bankeinzahlen");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    String money = this.addon.configuration().savedMoneyBalance().get();

    if (money != null && !money.isEmpty() && !money.equals("0")) {
      this.sendMessage("/bank einzahlen " + money);
      return true;
    }

    this.displayMessage(
        GradientUtils.gradient(" ᴜᴄᴇ ", TextColor.color(0x6a43e8), TextColor.color(0x405cd6))
            .append(Component.text("» ", NamedTextColor.DARK_GRAY))
            .append(Component.text("ᴅᴜ ʜᴀѕᴛ ᴍᴏᴍᴇɴᴛᴀɴ ᴋᴇɪɴ ɢᴇʟᴅ ᴀᴜꜱ ᴅᴇʀ ʜᴀɴᴅ.", NamedTextColor.RED))
    );
    return true;
  }
}