package com.hftf.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigBot {
    private static final Logger logger = LoggerFactory.getLogger(ConfigBot.class);
    private static final String CONFIG_PROPERTIES = "config.properties";
    private static final Properties properties = new Properties();

    static {
        try(InputStream input = ConfigBot.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES)) {
            if(input == null) {
                logger.error("Configuration file {} not found!", CONFIG_PROPERTIES);
            } else {
                properties.load(input);
                logger.info("Configuration loaded from {}", CONFIG_PROPERTIES);
            }
        } catch (IOException ioe) {
            logger.error("Error loading configuration file: ", ioe);
        }
    }

    public static String getBotToken() {
        return properties.getProperty("bot.token");
    }
}
