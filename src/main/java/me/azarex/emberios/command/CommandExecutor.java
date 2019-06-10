package me.azarex.emberios.command;

import me.azarex.emberios.command.base.Command;
import me.azarex.emberios.database.GuildData;
import me.azarex.emberios.scheduler.Scheduler;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class CommandExecutor extends ListenerAdapter {

    private final GuildData guildData;
    private final Scheduler scheduler;
    private final Function<String, Command> commandRetriever;

    public CommandExecutor(GuildData guildData, Scheduler scheduler, Function<String, Command> commandRetriever) {
        this.guildData = guildData;
        this.scheduler = scheduler;
        this.commandRetriever = commandRetriever;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.isWebhookMessage()) {
            return;
        }

        final long serverId = event.getGuild().getIdLong();
        final String messageRaw = event.getMessage().getContentRaw().trim();

        if (!messageRaw.startsWith(guildData.getPrefix(serverId))) {
            return;
        }

        Command command = commandRetriever.apply(messageRaw.substring(1).split(" ")[0]);

        if (command == null) {
            return;
        }

        String context = command.execute(event.getMessage(), messageRaw.split(" "));
        MessageEmbed embed = command.getIncorrectUsageEmbed(context);

        Message message = event.getMessage().getTextChannel().sendMessage(embed).complete();
        final long messageId = message.getIdLong();

        if (command.shouldClearOnSuccess() || !context.startsWith("success")) {
            scheduler.runAsyncTaskLater(() -> {
                if (command.shouldClearOnSuccess()) {
                    if (event.getChannel().getMessageById(messageId).complete() != null) {
                        message.delete().queue();
                    }
                }
            }, 5L, TimeUnit.SECONDS);
        }


    }
}
