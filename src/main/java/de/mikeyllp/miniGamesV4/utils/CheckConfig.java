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

public class CheckConfig {
    public static boolean checkAndFixingConfig(JavaPlugin plugin) {
        // Get the Config
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        ConfigurationSection loc = config.getConfigurationSection("spawn-location");

        boolean valid = true;

        // Check if the right validation is set
        // Games
        if (!config.isBoolean("HideAndSeek")) valid = false;
        if (!config.isBoolean("TicTacToe")) valid = false;
        if (!config.isBoolean("RockPaperScissors")) valid = false;

        // HAS
        if (!config.isInt("minPlayersPerHASGroup")) valid = false;
        if (!config.isInt("maxPlayersPerHASGroup")) valid = false;
        if (loc == null || !loc.isInt("x") || !loc.isInt("y") || !loc.isInt("z")) valid = false;

        // Checks if something is wrong with the config.
        if (!valid) {
            plugin.getLogger().warning("\u26A0 Invalid config.yml detected! The config will be reset to default values and backed up. \u26A0");
            try {
                File backupDir = new File(plugin.getDataFolder(), "CorruptedConfigs");

                if (!backupDir.exists()) backupDir.mkdirs();

                String timestamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
                File zipFile = new File(backupDir, "corrupted-config" + timestamp + ".zip");

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
