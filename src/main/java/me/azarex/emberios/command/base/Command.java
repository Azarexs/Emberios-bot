package me.azarex.emberios.command.base;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

public interface Command {

    MessageEmbed getIncorrectUsageEmbed(String context);
    boolean shouldClearOnSuccess();

    String execute(Message message, String[] arguments); // Returns a string for context to use.

}
