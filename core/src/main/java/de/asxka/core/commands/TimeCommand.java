package de.asxka.core.commands;

import de.asxka.core.utils.GradientUtils;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.misc.CaptureScreenshotEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeCommand extends Command {

  public TimeCommand() {
    super("time", "uhrzeit", "datum", "clock");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    String time = now.format(formatter);
    String date = now.format(dateFormatter);

    this.displayMessage(
        GradientUtils.gradient(" ᴜᴄᴇ ", TextColor.color(0x6a43e8), TextColor.color(0x405cd6))
            .append(Component.text("» ", NamedTextColor.DARK_GRAY))
        .append(Component.text("Es ist Aktuell ", NamedTextColor.GRAY))
            .append(GradientUtils.gradient(time, TextColor.color(0x6434eb), TextColor.color(0xa81adb)))
        .append(Component.text(" Uhr und der ", NamedTextColor.GRAY))
            .append(GradientUtils.gradient(date, TextColor.color(0x6434eb), TextColor.color(0xa81adb))));
    return true;
  }
}


