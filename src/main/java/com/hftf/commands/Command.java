package com.hftf.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface Command {
    String getName();
    String getDescription();
    void executeSlashCommand(SlashCommandInteractionEvent event);
}
