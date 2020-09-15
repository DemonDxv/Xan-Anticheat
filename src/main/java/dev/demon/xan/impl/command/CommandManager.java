package dev.demon.xan.impl.command;


import dev.demon.xan.impl.command.commands.MainCommand;
import dev.demon.xan.utils.command.CommandUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommandManager {

    private List<Command> commandList = new ArrayList<>();

    public CommandManager() {
        addCommand(new Command(new MainCommand("xan"), "xan", null, "Main command.", true));
        addCommand(new Command(new MainCommand("xan"), "xan info", "/Xan info <player>", "Information command.", true));
        addCommand(new Command(new MainCommand("xan"), "xan alerts", "/Xan alerts", "Alerts command.", true));
    }

    private void addCommand(Command... commands) {
        for (Command command : commands) {
            commandList.add(command);
            if (command.isEnabled()) CommandUtils.registerCommand(command);
        }
    }
}

