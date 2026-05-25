package de.asxka.core.commands;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;

public class WPSCommand extends Command {

    public WPSCommand() {
        super("wps");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        String commandToExecute = "/wanteds";

        if (arguments.length > 0) {
            commandToExecute += " " + String.join(" ", arguments);
        }

        Laby.labyAPI().minecraft().chatExecutor().chat(commandToExecute, false);

        return true;
    }
}

