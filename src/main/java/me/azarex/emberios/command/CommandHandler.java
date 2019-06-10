package me.azarex.emberios.command;

import me.azarex.emberios.command.base.Command;
import me.azarex.emberios.database.GuildData;
import me.azarex.emberios.scheduler.Scheduler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {

    private final CommandExecutor commandExecutor;
    private final Map<String, Command> commands = new HashMap<>();

    public CommandHandler(GuildData guildData, Scheduler scheduler) {
        commandExecutor = new CommandExecutor(guildData, scheduler, command -> commands.get(command.toLowerCase()));
    }

    public CommandHandler register(String commandName, Command command) {
        return register(commandName, new String[0], command);
    }

    public CommandHandler register(String commandName, String[] aliases, Command command) {
        commands.put(commandName, command);
        Arrays.stream(aliases).forEach(alias -> commands.put(alias, command));
        return this;
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }
}
