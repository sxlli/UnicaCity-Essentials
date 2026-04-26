package de.asxka.core.commands;

import de.asxka.core.utils.GradientUtils;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import de.asxka.core.UnicaCityEssentials;
import de.asxka.core.configurations.UCEConfiguration;
import de.asxka.core.configurations.EigenbedarfSubSettings;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.notification.Notification;

public class EigenbedarfCommand extends Command {

  private final UnicaCityEssentials addon;

  public EigenbedarfCommand(UnicaCityEssentials addon) {
    super("eigenbedarf", "eb");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    UCEConfiguration config = this.addon.configuration();
    EigenbedarfSubSettings eigenbedarf = config.eigenbedarf();

    String krauterMenge = eigenbedarf.krauterMenge().get();
    String krauterReinheit = eigenbedarf.krauterReinheit().get().getValue();
    String pulverMenge = eigenbedarf.pulverMenge().get();
    String pulverReinheit = eigenbedarf.pulverReinheit().get().getValue();

    boolean hasAnyItem = (krauterMenge != null && !krauterMenge.trim().isEmpty()) ||
        (pulverMenge != null && !pulverMenge.trim().isEmpty());

    if (!hasAnyItem) {
      this.Notification();
      return true;
    }
    this.displayMessage(
    GradientUtils.gradient(" ᴜᴄᴇ ", TextColor.color(0x6a43e8), TextColor.color(0x405cd6))
        .append(Component.text("Lagere aus:", NamedTextColor.WHITE)));

    if (krauterMenge != null && !krauterMenge.trim().isEmpty()) {
      String menge = krauterMenge.trim() + "g";
      String reinheit = (krauterReinheit != null && !krauterReinheit.trim().isEmpty()) ? krauterReinheit.trim() : "1";
      String command = "/dbank auslagern Kräuter " + menge + " " + reinheit;
      String total = "Kräuter " + menge + " " + reinheit + "er Reinheit";
      this.sendMessage(command);
      this.displayMessage(Component.text("  » ", NamedTextColor.GRAY)
          .append(Component.text(total, NamedTextColor.GOLD)));
    }

    if (pulverMenge != null && !pulverMenge.trim().isEmpty()) {
      String menge = pulverMenge.trim() + "g";
      String reinheit = (pulverReinheit != null && !pulverReinheit.trim().isEmpty()) ? pulverReinheit.trim() : "0";
      String command = "/dbank auslagern Pulver " + menge + " " + reinheit;
      String total = "Pulver " + menge + " " + reinheit + "er Reinheit";
      this.sendMessage(command);
      this.displayMessage(Component.text("  » ", NamedTextColor.GRAY)
          .append(Component.text(total, NamedTextColor.GOLD)));
    }

    return true;
  }

  private void Notification() {

    Notification.Builder builder = Notification.builder()
        .title(GradientUtils.gradient("ꜰᴇʜʟᴇʀ", TextColor.color(0xe81717), TextColor.color(0x911010)))
        .text(Component.text("ᴅᴜ ʜᴀѕᴛ ʙɪѕʜᴇʀ ᴋᴇɪɴ ᴇɪɢᴇɴʙᴇᴅᴀʀꜰ ɪɴ ᴅᴇɴ ᴀᴅᴅᴏɴ ᴇɪɴѕᴛᴇʟʟᴜɴɢᴇɴ ꜰᴇѕᴛɢᴇʟᴇɢᴛ.", NamedTextColor.RED))
        .icon(Icon.url("https://cdn-icons-png.flaticon.com/512/463/463612.png"))
        .duration(5000);
    builder.buildAndPush();
  }
}




