package de.asxka.core.commands;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;

public class WPSCommand extends Command {

    public WPSCommand() {
        super("wps");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        // Wir bauen den eigentlichen Befehl auf. Standardmäßig "/wanteds"
        String commandToExecute = "/wanteds";

        // Falls der Spieler noch Argumente mitgibt (z.B. "/wps Name"),
        // hängen wir diese einfach an den "/wanteds" Befehl an.
        if (arguments.length > 0) {
            commandToExecute += " " + String.join(" ", arguments);
        }

        // Sende den "/wanteds" Command (mit evtl. Argumenten) an den Server
        Laby.labyAPI().minecraft().chatExecutor().chat(commandToExecute, false);

        // Gebe true zurück, damit LabyMod weiß, dass der Befehl verarbeitet wurde
        return true;
    }
}

