package de.asxka.core.configurations;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
//@SpriteTexture("settings") //kommt später
public class SolaraConfiguration extends AddonConfig {

  @SwitchSetting
  @SpriteSlot(y = 0)
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SettingSection("eigenbedarf")
  @SpriteSlot(y = 1)
  private final EigenbedarfSubSettings eigenbedarf = new EigenbedarfSubSettings();

  @SettingSection("customreinf")
  @SpriteSlot(y = 2)
  private final CustomReinfSubSettings customReinf = new CustomReinfSubSettings();

  @SettingSection("factionsMemberColor")
  @SpriteSlot(y = 3)
  private final FactionMemberColorSubSettings factionMemberColor = new FactionMemberColorSubSettings();

  @SettingSection("messages")
  @SpriteSlot(y = 4)
  @SwitchSetting
  private final ConfigProperty<Boolean> FriendlistNotify = new ConfigProperty<>(true);

  @SpriteSlot(y = 5)
  @SwitchSetting
  private final ConfigProperty<Boolean> transferTablistColors = new ConfigProperty<>(true);

  private final ConfigProperty<String> savedBankBalance = new ConfigProperty<>("0");
  private final ConfigProperty<String> savedMoneyBalance = new ConfigProperty<>("0");
  private final ConfigProperty<String> savedFishingExp = new ConfigProperty<>("0 / 0");

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public EigenbedarfSubSettings eigenbedarf() {
    return this.eigenbedarf;
  }

  public CustomReinfSubSettings customReinf() {
    return this.customReinf;
  }

  public FactionMemberColorSubSettings factionMemberColor() {
    return this.factionMemberColor;
  }

  public ConfigProperty<Boolean> FriendlistNotify() {
    return this.FriendlistNotify;
  }

  public ConfigProperty<Boolean> transferTablistColors() {
    return this.transferTablistColors;
  }

  public ConfigProperty<String> savedBankBalance() {
    return this.savedBankBalance;
  }

  public ConfigProperty<String> savedMoneyBalance() {
    return this.savedMoneyBalance;
  }

  public ConfigProperty<String> savedFishingExp() {
    return this.savedFishingExp;
  }
}
