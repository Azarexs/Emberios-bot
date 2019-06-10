package me.azarex.emberios.listeners;

import me.azarex.emberios.database.GuildData;
import me.azarex.emberios.database.UserData;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.time.LocalDateTime;

public class ChatListener extends ListenerAdapter {

    private final GuildData guildData;

    public ChatListener(GuildData guildData) {
        this.guildData = guildData;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        final long serverId = event.getGuild().getIdLong();
        final long userId = event.getAuthor().getIdLong();
        final UserData user = guildData.getUserData(serverId, userId);
        final LocalDateTime dateTime = LocalDateTime.now();

        if (dateTime.isBefore(user.getExpTime().plusMinutes(1))) {
            return;
        }

        user.setExp(user.getExp() + 50);
        user.setExpTime(LocalDateTime.now());
    }
}
