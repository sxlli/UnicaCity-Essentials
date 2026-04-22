package de.asxka.core.configurations;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.MethodOrder;

public class CustomReinfSubSettings extends Config {

  @SwitchSetting
  private final ConfigProperty<Boolean> enableCustomReinf = new ConfigProperty<>(true);

  @SettingSection("messages")
  @TextFieldSetting
  private final ConfigProperty<String> CustomReinf = new ConfigProperty<>("");

  @MethodOrder(after = "CustomReinf")
  @ButtonSetting
  public void testReinf(Setting setting) {
    String type = "Test";
    String playerName = Laby.labyAPI().getName();
    String location = "Bank";
    String distance = "123";

    String customFormat = this.CustomReinf.get();
    Component newMessage;

    if (customFormat != null && !customFormat.trim().isEmpty()) {
      String replacedFormat = customFormat
          .replace("%type%", type)
          .replace("%player%", playerName)
          .replace("%location%", location)
          .replace("%distance%", distance + "m");
      newMessage = net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer.legacyAmpersand().deserialize(replacedFormat);
    } else {
      newMessage = Component.text()
          .append(Component.text("Reinforcement! ", net.labymod.api.client.component.format.NamedTextColor.RED).decorate(net.labymod.api.client.component.format.TextDecoration.BOLD))
          .append(Component.text(playerName, net.labymod.api.client.component.format.NamedTextColor.AQUA))
          .append(Component.text(" \u2013 ", net.labymod.api.client.component.format.NamedTextColor.GRAY))
          .append(Component.text(location, net.labymod.api.client.component.format.NamedTextColor.AQUA))
          .append(Component.text(" \u2013 ", net.labymod.api.client.component.format.NamedTextColor.GRAY))
          .append(Component.text(distance + "m", net.labymod.api.client.component.format.NamedTextColor.DARK_AQUA))
          .build();
    }

    Laby.labyAPI().minecraft().chatExecutor().displayClientMessage(newMessage);
  }

  public ConfigProperty<String> CustomReinf() {
    return this.CustomReinf;
  }

  public ConfigProperty<Boolean> enableCustomReinf() {
    return this.enableCustomReinf;
  }

}
