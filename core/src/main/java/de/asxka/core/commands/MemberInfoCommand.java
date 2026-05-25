package de.asxka.core.commands;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;

public class MemberInfoCommand extends Command {

    public MemberInfoCommand() {
        super("memberinfo");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        if (arguments.length > 0 && arguments[0].equalsIgnoreCase("cops")) {
            Laby.labyAPI().minecraft().chatExecutor().chat("/memberinfo polizei", false);
            return true;
        }
        else if (arguments.length > 0 && arguments[0].equalsIgnoreCase("kf")) {
            Laby.labyAPI().minecraft().chatExecutor().chat("/memberinfo kerzakov", false);
            return true;
        }
        return false;
    }
}

