package me.azarex.emberios.command.base.impl;

import me.azarex.emberios.command.base.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.List;

public class ClearCommand implements Command {

    @Override
    public MessageEmbed getIncorrectUsageEmbed(String context) {
        String[] contextArray = context.split(" ");

        switch (contextArray[0]) {
            case "wrong-amount-of-arguments":
                return new EmbedBuilder()
                        .setColor(Color.decode("#f44141"))
                        .setTitle("Incorrect usage!")
                        .setDescription("<> = required, [] = optional, (x, y) = range")
                        .addField("Correct Usage:", "/clear <amount(2-99)> [channel]", true)
                        .build();
            case "range-limit":
                return new EmbedBuilder()
                        .setColor(Color.decode("#f44141"))
                        .setTitle("Incorrect Usage!")
                        .setDescription(contextArray[1] + " is not between 2 and 99 messages")
                        .build();
            case "channel-does-not-exist":
                return new EmbedBuilder()
                        .setColor(Color.decode("#f44141"))
                        .setTitle("Incorrect Usage!")
                        .setDescription("Channel \"" + contextArray[1] + "\" does not exist!")
                        .build();
            case "not-enough-messages-in-channel":
                return new EmbedBuilder()
                        .setColor(Color.decode("#f44141"))
                        .setTitle("Incorrect usage!")
                        .setDescription("Channel <#" + contextArray[1] + "> does not have greater than 1 message!")
                        .build();
            case "success":
                return new EmbedBuilder()
                        .setColor(Color.decode("#42f445"))
                        .setTitle("Success!")
                        .setDescription("You have successfully cleared " + contextArray[1] + " messages from <#" + contextArray[2] + ">")
                        .build();
            default:
                return new EmbedBuilder()
                        .setTitle("Unexpected context!")
                        .setDescription("Contact the developer! ")
                        .build();
        }
    }

    @Override
    public boolean shouldClearOnSuccess() {
        return true;
    }

    @Override
    public String execute(Message message, String[] arguments) {
        final TextChannel channel = message.getTextChannel();

        if (arguments.length == 1) {
            return "wrong-amount-of-arguments";
        }

        if (!isValidNumber(arguments[1])) {
            return "wrong-amount-of-arguments";
        }

        int amount = Integer.parseInt(arguments[1]);

        if (amount < 2 || amount > 99) {
            return "range-limit " + amount;
        }

        if (arguments.length == 2) {
            return handleSuccess(channel, amount);
        }

        TextChannel targetChannel = getChannel(message.getGuild(), arguments[2]);

        if (targetChannel == null) {
            return "channel-does-not-exist " + arguments[2];
        }

        message.delete().complete();

        return handleSuccess(targetChannel, amount);
    }

    private boolean isValidNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private TextChannel getChannel(Guild guild, String channel) {
        final String parsedChannel = channel.replaceAll("([<#>])", "");

        return guild.getTextChannels().stream()
                .filter(textChannel -> textChannel.getId().equals(parsedChannel))
                .findFirst().orElse(null);
    }

    private String handleSuccess(TextChannel channel, int amountToRetrieve) {
        List<Message> messages = channel.getHistory().retrievePast(amountToRetrieve + 1).complete();

        if (messages.size() < 2) {
            return "not-enough-messages-in-channel " + channel.getId();
        }

        channel.deleteMessages(messages).complete();

        return "success " + (amountToRetrieve) + " " + channel.getId();
    }
}
