package de.asxka.core.commands;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;

public class MemberInfoCommand extends Command {

    public MemberInfoCommand() {
        super("memberinfo");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        // Wenn der Command mit "cops" ausgeführt wird, z.B. /memberinfo cops
        if (arguments.length > 0 && arguments[0].equalsIgnoreCase("cops")) {
            // Sende den eigentlich richtigen Command an den Server
            Laby.labyAPI().minecraft().chatExecutor().chat("/memberinfo polizei", false);

            // true bedeutet: Wir haben den Command verarbeitet, LabyMod soll hier abbrechen.
            return true;
        }

        // Wenn etwas anderes wie z.B. /memberinfo medics oder /memberinfo polizei eingegeben wird,
        // geben wir false zurück. LabyMod merkt dann "Aha, das Addon übernimmt diesen Befehl nicht"
        // und leitet ihn ganz normal automatisch an den Server weiter!
        return false;
    }
}

