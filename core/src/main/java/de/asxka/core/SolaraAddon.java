package de.asxka.core;

import de.asxka.core.commands.EinzahlenCommand;
import de.asxka.core.commands.MemberInfoCommand;
import de.asxka.core.commands.WPSCommand;
import de.asxka.core.commands.SyncCommand;
import de.asxka.core.listener.FriendNotificationListener;
import de.asxka.core.listener.Jobs.HochseefischerListener;
import de.asxka.core.listener.Jobs.JobDropListener;
import de.asxka.core.listener.Faction.FactionMemberDesignListener;
import de.asxka.core.listener.DutyNameTagListener;
import de.asxka.core.listener.Faction.FactionSyncListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.Subscribe;
import de.asxka.core.commands.EigenbedarfCommand;
import de.asxka.core.commands.TimeCommand;
import de.asxka.core.listener.Faction.ReinforcementListener;
import de.asxka.core.listener.Faction.WantedListener;
import de.asxka.core.listener.SocialMediaChatListener;
import de.asxka.core.configurations.SolaraConfiguration;

@AddonMain
public class SolaraAddon extends LabyAddon<SolaraConfiguration> {

  private boolean onUnicaCity = false;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.registerCommands();
    this.registerListeners();
    //this.registerTags();
    this.registerWidgets();

    this.labyAPI().eventBus().registerListener(this);
  }

  public boolean isOnUnicaCity() {
    try {
      if (this.labyAPI().serverController().getCurrentServerData() != null && this.labyAPI().serverController().getCurrentServerData().address() != null) {
        String address = this.labyAPI().serverController().getCurrentServerData().address().getHost().toLowerCase();
        return address.contains("unicacity") || this.onUnicaCity;
      }
    } catch (Exception e) {
      // ignore
    }
    return this.onUnicaCity;
  }

  @Subscribe
  public void onServerJoin(ServerJoinEvent event) {
    try {
      if (event.serverData() != null && event.serverData().address() != null) {
        String address = event.serverData().address().getHost().toLowerCase();
        this.onUnicaCity = address.contains("unicacity.eu") || address.contains("unicacity.de") || address.contains("unicacity");
      }
    } catch (Exception e) {
      this.onUnicaCity = false;
    }
  }

  @Subscribe
  public void onServerDisconnect(ServerDisconnectEvent event) {
    this.onUnicaCity = false;
  }

  @Override
  protected Class<SolaraConfiguration> configurationClass() {
    return SolaraConfiguration.class;
  }

  private void registerCommands() {
    this.registerCommand(new TimeCommand());
    this.registerCommand(new EigenbedarfCommand(this));
    this.registerCommand(new WPSCommand());
    this.registerCommand(new MemberInfoCommand());
    this.registerCommand(new EinzahlenCommand(this));
    this.registerCommand(new SyncCommand(this));
  }

  private void registerListeners() {
    this.registerListener(new ReinforcementListener(this));
    this.registerListener(new WantedListener());
    this.registerListener(new SocialMediaChatListener());
    this.registerListener(new JobDropListener());
    this.registerListener(new FriendNotificationListener(this));
    this.registerListener(new FactionMemberDesignListener(this));
    this.registerListener(new DutyNameTagListener(this));
    this.registerListener(new FactionSyncListener(this));
    this.registerListener(new HochseefischerListener());
  }

  private void registerTags() {
    //this.labyAPI().tagRegistry().register("solara_subtitle", PositionType.BELOW_NAME, NameTagListener.create());
  }

  private void registerWidgets() {
    this.labyAPI().hudWidgetRegistry().register(new de.asxka.core.widgets.HealthWidget("health"));
    this.labyAPI().hudWidgetRegistry().register(new de.asxka.core.widgets.BankWidget("bank", this));
    this.labyAPI().hudWidgetRegistry().register(new de.asxka.core.widgets.FishingWidget("fishing", this));
  }
}
