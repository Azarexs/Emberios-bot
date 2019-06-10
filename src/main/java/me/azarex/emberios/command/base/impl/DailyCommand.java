package me.azarex.emberios.command.base.impl;

import me.azarex.emberios.command.base.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class DailyCommand implements Command {
    @Override
    public MessageEmbed getIncorrectUsageEmbed(String context) {
        return null;
    }

    @Override
    public boolean shouldClearOnSuccess() {
        return false;
    }

    @Override
    public String execute(Message message, String[] arguments) {
        return null;
    }
}
