package de.asxka.core.commands;

import de.asxka.core.configurations.enums.FactionType;
import de.asxka.core.utils.GradientUtils;
import de.asxka.core.utils.NotificationUtils;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import de.asxka.core.SolaraAddon;
import net.labymod.api.client.component.format.TextColor;

public class SyncCommand extends Command {

  private final SolaraAddon addon;

  public SyncCommand(SolaraAddon addon) {
    super("sync");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.configuration().factionMemberColor().enableFactionMemberColor().get()) {
      Laby.labyAPI().minecraft().chatExecutor().displayClientMessage(
          Component.text("Das Faction-Color Feature ist deaktiviert!", NamedTextColor.RED)
      );
      return true;
    }

    String faction = this.addon.configuration().factionMemberColor().faction().get().getName();
    String commandName = this.addon.configuration().factionMemberColor().faction().get().getCommandName();
    FactionType type = this.addon.configuration().factionMemberColor().faction().get();

    if (type == FactionType.ZIVILIST) {
      de.asxka.core.utils.FactionCache.save(new java.util.ArrayList<>());
      NotificationUtils.pushComponentNotification(
          GradientUtils.gradient("ᴜɴɪᴄᴀᴄɪᴛʏ ᴇѕѕᴇɴᴛɪᴀʟѕ", TextColor.color(0xa17cf7), TextColor.color(0x9337c4)),
          Component.text("Fraktion auf Zivilist gesetzt (Config resettet)", NamedTextColor.GRAY)
      );
      return true;
    }

    NotificationUtils.pushComponentNotification(
        GradientUtils.gradient("ᴜɴɪᴄᴀᴄɪᴛʏ ᴇѕѕᴇɴᴛɪᴀʟѕ", TextColor.color(0xa17cf7), TextColor.color(0x9337c4)),
        Component.text("Synchronisiere Mitglieder der Fraktion " + faction + "...", NamedTextColor.GRAY)
    );

    Laby.labyAPI().minecraft().chatExecutor().chat("/memberinfoall " + commandName, false);

    return true;
  }
}
