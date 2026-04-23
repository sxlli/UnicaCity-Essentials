package de.asxka.core.configurations;

import de.asxka.core.configurations.enums.FactionColor;
import de.asxka.core.configurations.enums.FactionType;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class FactionMemberColorSubSettings extends Config {

  @SwitchSetting
  private final ConfigProperty<Boolean> enableFactionMemberColor = new ConfigProperty<>(true);

  @DropdownSetting
  private final ConfigProperty<FactionType> faction = new ConfigProperty<>(FactionType.ZIVILIST);

  @DropdownSetting
  private final ConfigProperty<FactionColor> color = new ConfigProperty<>(FactionColor.BLUE);

  @SwitchSetting
  private final ConfigProperty<Boolean> enableAllianceMemberColor = new ConfigProperty<>(false);

  @DropdownSetting
  private final ConfigProperty<FactionType> allianceFaction = new ConfigProperty<>(FactionType.ZIVILIST);

  @DropdownSetting
  private final ConfigProperty<FactionColor> allianceColor = new ConfigProperty<>(FactionColor.DARK_BLUE);


  public ConfigProperty<Boolean> enableFactionMemberColor() {
    return this.enableFactionMemberColor;
  }

  public ConfigProperty<FactionType> faction() {
    return this.faction;
  }

  public ConfigProperty<FactionColor> color() {
    return this.color;
  }

  public ConfigProperty<Boolean> enableAllianceMemberColor() {
    return this.enableAllianceMemberColor;
  }

  public ConfigProperty<FactionType> allianceFaction() {
    return this.allianceFaction;
  }

  public ConfigProperty<FactionColor> allianceColor() {
    return this.allianceColor;
  }
}
