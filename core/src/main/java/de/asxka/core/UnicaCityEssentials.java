package de.asxka.core;

import de.asxka.core.commands.EinzahlenCommand;
import de.asxka.core.commands.MemberInfoCommand;
import de.asxka.core.commands.WPSCommand;
import de.asxka.core.listener.BankInfoListener;
import de.asxka.core.listener.CarListener;
import de.asxka.core.listener.Faction.WantedMessagesListener;
import de.asxka.core.listener.FriendNotificationListener;
import de.asxka.core.listener.Jobs.HochseefischerListener;
import de.asxka.core.listener.Jobs.JobDropListener;
import de.asxka.core.listener.Faction.FactionMemberDesignListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.Subscribe;
import de.asxka.core.commands.EigenbedarfCommand;
import de.asxka.core.commands.TimeCommand;
import de.asxka.core.listener.Faction.ReinforcementListener;
import de.asxka.core.listener.SocialMediaChatListener;
import de.asxka.core.configurations.UCEConfiguration;
import de.asxka.core.listener.ActivityListener;
import de.asxka.core.utils.PatternUtils;

@AddonMain
public class UnicaCityEssentials extends LabyAddon<UCEConfiguration> {

  private boolean onUnicaCity = false;
  private de.asxka.core.widgets.ActivityWidget activityWidget;
  private de.asxka.core.widgets.CarLockWidget carLockWidget;
  private de.asxka.core.widgets.BankWidget bankWidget;

  public de.asxka.api.InventoryClicker inventoryClicker;

  @Override
  protected void enable() {
    this.activityWidget = new de.asxka.core.widgets.ActivityWidget("activity");
    this.carLockWidget = new de.asxka.core.widgets.CarLockWidget("carlock");
    this.bankWidget = new de.asxka.core.widgets.BankWidget("bank", this);

    this.inventoryClicker = ((de.asxkaa.api.generated.ReferenceStorage) this.referenceStorageAccessor()).inventoryClicker();

    this.registerSettingCategory();

    this.registerCommands();
    this.registerListeners();
    this.registerWidgets();

    this.labyAPI().eventBus().registerListener(this);
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
  protected Class<UCEConfiguration> configurationClass() {
    return UCEConfiguration.class;
  }

  private void registerCommands() {
    this.registerCommand(new TimeCommand());
    this.registerCommand(new EinzahlenCommand(this));
    this.registerCommand(new EigenbedarfCommand(this));
    this.registerCommand(new WPSCommand());
    this.registerCommand(new MemberInfoCommand());
  }

  private void registerListeners() {
    this.registerListener(new ActivityListener(this.activityWidget, new PatternUtils()));
    this.registerListener(new ReinforcementListener(this));
    this.registerListener(new WantedMessagesListener());
    this.registerListener(new SocialMediaChatListener());
    this.registerListener(new JobDropListener());
    this.registerListener(new FriendNotificationListener(this));
    this.registerListener(new FactionMemberDesignListener(this));
    this.registerListener(new BankInfoListener(this));
    this.registerListener(new HochseefischerListener());
    this.registerListener(new CarListener(this.inventoryClicker, this.carLockWidget));
  }

  private void registerWidgets() {
    this.labyAPI().hudWidgetRegistry().register(this.activityWidget);
    this.labyAPI().hudWidgetRegistry().register(new de.asxka.core.widgets.HealthWidget("health"));
    this.labyAPI().hudWidgetRegistry().register(new de.asxka.core.widgets.FishingWidget("fishing", this));
    this.labyAPI().hudWidgetRegistry().register(new de.asxka.core.widgets.PayDayWidget("payday"));
    this.labyAPI().hudWidgetRegistry().register(new de.asxka.core.widgets.AbsorptionWidget("absorption"));
    this.labyAPI().hudWidgetRegistry().register(this.carLockWidget);
    this.labyAPI().hudWidgetRegistry().register(this.bankWidget);
  }

  public de.asxka.core.widgets.BankWidget bankWidget() {
    return this.bankWidget;
  }
}
