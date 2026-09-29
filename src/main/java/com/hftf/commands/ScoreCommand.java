package com.hftf.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class ScoreCommand implements Command {
    @Override
    public String getName() {
        return "score";
    }

    @Override
    public String getDescription() {
        return "Shows the score of the player's latest match";
    }

    @Override
    public void executeSlashCommand(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("User's last match score");
        embedBuilder.setDescription("Shows the score of the player's latest match.");
        embedBuilder.setColor(new Color(255, 64, 252));
        embedBuilder.addField("Player", "0", true);
        embedBuilder.addField("Opponent", "0", true);
        embedBuilder.setFooter("This is only a prototype");

        MessageEmbed embed = embedBuilder.build();
        event.replyEmbeds(embed).queue();
    }
}
