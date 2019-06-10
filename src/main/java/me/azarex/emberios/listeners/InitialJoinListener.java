package me.azarex.emberios.listeners;

import me.azarex.emberios.database.GuildData;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class InitialJoinListener extends ListenerAdapter {

    private final GuildData guildData;

    public InitialJoinListener(GuildData guildData) {
        this.guildData = guildData;
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        guildData.createGuildData(event.getGuild().getIdLong());
    }
}
