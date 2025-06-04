package de.mikeyllp.miniGamesV4.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CheckConfigUtils {
    public static boolean checkAndFixingConfig(JavaPlugin plugin) {
        // Get the Config
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        ConfigurationSection loc = config.getConfigurationSection("spawn-location");

        boolean valid = true;

        // Check if the right validation is set
        // General
        if (!config.isString("language")) {
            plugin.getLogger().warning("Config key 'language' is missing or not a string.");
            valid = false;
        }
        if (!config.isString("prefix")) {
            plugin.getLogger().warning("Config key 'prefix' is missing or not a string.");
            valid = false;
        }
        if (!config.isString("command")) {
            plugin.getLogger().warning("Config key 'command' is missing or not a string.");
            valid = false;
        }

        // Games
        if (!config.isBoolean("HideAndSeek")) {
            plugin.getLogger().warning("Config key 'HideAndSeek' is missing or not a boolean.");
            valid = false;
        }
        if (!config.isBoolean("RockPaperScissors")) {
            plugin.getLogger().warning("Config key 'RockPaperScissors' is missing or not a boolean.");
            valid = false;
        }
        if (!config.isBoolean("TicTacToe")) {
            plugin.getLogger().warning("Config key 'TicTacToe' is missing or not a boolean.");
            valid = false;
        }


        // HAS
        if (!config.isInt("minPlayersPerHASGroup")) {
            plugin.getLogger().warning("Config key 'HideAndSeek' is missing or not a boolean.");
            valid = false;
        }
        if (!config.isInt("maxPlayersPerHASGroup")) {
            plugin.getLogger().warning("Config key 'maxPlayersPerHASGroup' is missing or not an integer.");
            valid = false;
        }
        if (!config.isInt("maxSeekersPerHASGroup")) {
            plugin.getLogger().warning("Config key 'maxSeekersPerHASGroup' is missing or not an integer.");
            valid = false;
        }
        if (loc == null || !loc.isString("world") || !loc.isDouble("x") || !loc.isDouble("y") || !loc.isDouble("z")) {
            plugin.getLogger().warning("Config key 'spawn-location' is missing or not properly set.");
            valid = false;
        }
        if (!config.isInt("timeAutoStartHASGroup")) {
            plugin.getLogger().warning("Config key 'timeAutoStartHASGroup' is missing or not an integer.");
            valid = false;
        }
        if (!config.isInt("playTimeHAS")) {
            plugin.getLogger().warning("Config key 'playTimeHAS' is missing or not an integer.");
            valid = false;
        }
        if (!config.isInt("hideTimeHAS")) {
            plugin.getLogger().warning("Config key 'hideTimeHAS' is missing or not an integer.");
            valid = false;
        }
        if (!config.isInt("HASHints")) {
            plugin.getLogger().warning("Config key 'HASHints' is missing or not an integer.");
            valid = false;
        }
        if (!config.isInt("HintTimeHAS")) {
            plugin.getLogger().warning("Config key 'HintTimeHAS' is missing or not an integer.");
            valid = false;
        }

        // Checks if something is wrong with the config.
        if (!valid) {
            plugin.getLogger().warning("\u26A0 Invalid config.yml detected! The config will be reset to default values and backed up. \u26A0");
            try {
                File backupDir = new File(plugin.getDataFolder(), "CorruptedConfigs");

                if (!backupDir.exists()) backupDir.mkdirs();

                String timestamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
                File zipFile = new File(backupDir, "corrupted-config_" + timestamp + ".zip");

                try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
                    zipOut.putNextEntry(new ZipEntry("config.yml"));
                    Files.copy(configFile.toPath(), zipOut);
                    zipOut.closeEntry();
                }
                // Delete the Corrupted Config
                configFile.delete();

                // Create a new config with default values
                plugin.saveDefaultConfig();
                plugin.getLogger().warning("Config has been reset to default values and backed up successfully!");

            } catch (IOException e) {
                plugin.getLogger().warning("\u26A0 No backup can be created: " + e.getMessage());
            }
        }
        return valid;
    }
}
