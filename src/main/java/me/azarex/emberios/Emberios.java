package me.azarex.emberios;

import me.azarex.emberios.command.CommandHandler;
import me.azarex.emberios.command.base.impl.ClearCommand;
import me.azarex.emberios.database.GuildData;
import me.azarex.emberios.database.impl.MongoDB;
import me.azarex.emberios.listeners.ChatListener;
import me.azarex.emberios.listeners.InitialJoinListener;
import me.azarex.emberios.scheduler.Scheduler;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class Emberios {

    private Emberios(String token, String databaseUri) throws LoginException {
        GuildData guildData = new MongoDB(databaseUri);
        Scheduler scheduler = new Scheduler();
        CommandHandler commandHandler = new CommandHandler(guildData, scheduler)
                .register("clear", new ClearCommand());

        ShardManager shardManager = new DefaultShardManagerBuilder()
                .setToken(token)
                .setShardsTotal(1)
                .addEventListeners(
                        commandHandler.getCommandExecutor(),
                        new InitialJoinListener(guildData),
                        new ChatListener(guildData)
                ).build();
    }

    public static void main(String[] args) {
        try {
            new Emberios(args[0], args[1]);
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

}
