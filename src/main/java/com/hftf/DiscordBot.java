package com.hftf;

import com.hftf.configs.ConfigBot;
import com.hftf.listeners.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

public class DiscordBot {
    private static final Logger logger = LoggerFactory.getLogger(DiscordBot.class);
    private static JDA jda;

    public static void main(String[] args) {
        String botToken = ConfigBot.getBotToken();
        if(botToken == null || botToken.trim().isEmpty()) {
            logger.error("Bot token not found in config.properties. Please provide a valid token");
            return;
        }

        try{
            jda = JDABuilder.createDefault(botToken)
                    .enableIntents(EnumSet.allOf(GatewayIntent.class))
                    .addEventListeners(new CommandListener())
                    .build();

            jda.awaitReady();
            logger.info("Bot is online and ready!");

            registerSlashCommands();
        } catch (Exception e) {
            logger.error("Error starting the bot.", e);
        }
    }

    private static void registerSlashCommands() {
        if(jda == null) {
            logger.error("JDA instance is not initialized. Cannot register slash commands.");
            return;
        }

        logger.info("Registering Slash Commands...");
        jda.updateCommands()
                .addCommands(
                        Commands.slash("score", "Shows the score of the player's latest match")
                                .addOption(OptionType.STRING, "username", "Player's username")
                ).queue(success -> logger.info("Slash commands registered successfully!"),
                        failure -> logger.error("Failed to register slash commands: ", failure));
    }

}
