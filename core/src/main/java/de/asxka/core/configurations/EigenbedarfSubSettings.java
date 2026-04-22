package de.asxka.core.configurations;

import de.asxka.core.configurations.enums.ReinheitLevel;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

public class EigenbedarfSubSettings extends Config {

  @SettingSection("amounts")
  @TextFieldSetting
  private final ConfigProperty<String> pulverMenge = new ConfigProperty<>("");

  @TextFieldSetting
  private final ConfigProperty<String> kräuterMenge = new ConfigProperty<>("");

  @SettingSection("purities")
  @DropdownSetting
  private final ConfigProperty<ReinheitLevel> pulverReinheit = new ConfigProperty<>(ReinheitLevel.LEVEL_0);

  @DropdownSetting
  private final ConfigProperty<ReinheitLevel> kräuterReinheit = new ConfigProperty<>(ReinheitLevel.LEVEL_0);

  public ConfigProperty<String> krauterMenge() {
    return this.kräuterMenge;
  }

  public ConfigProperty<ReinheitLevel> krauterReinheit() {
    return this.kräuterReinheit;
  }

  public ConfigProperty<String> pulverMenge() {
    return this.pulverMenge;
  }

  public ConfigProperty<ReinheitLevel> pulverReinheit() {
    return this.pulverReinheit;
  }
}

