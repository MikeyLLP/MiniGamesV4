package de.mikeyllp.miniGamesV4.utils

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object CheckConfigUtils {
    fun checkAndFixingConfig(plugin: JavaPlugin): Boolean {
        // Get the Config
        val configFile = File(plugin.getDataFolder(), "config.yml")
        val config: FileConfiguration = YamlConfiguration.loadConfiguration(configFile)

        val loc = config.getConfigurationSection("spawn-location")
        val small = config.getConfigurationSection("small-modus")

        var valid = true

        // Check if the right validation is set
        // General
        if (!config.isString("language")) {
            plugin.getLogger().warning("Config key 'language' is missing or not a string.")
            valid = false
        }
        if (!config.isString("prefix")) {
            plugin.getLogger().warning("Config key 'prefix' is missing or not a string.")
            valid = false
        }
        if (!config.isString("command")) {
            plugin.getLogger().warning("Config key 'command' is missing or not a string.")
            valid = false
        }

        // Games
        if (!config.isBoolean("HideAndSeek")) {
            plugin.getLogger().warning("Config key 'HideAndSeek' is missing or not a boolean.")
            valid = false
        }
        if (!config.isBoolean("RockPaperScissors")) {
            plugin.getLogger().warning("Config key 'RockPaperScissors' is missing or not a boolean.")
            valid = false
        }
        if (!config.isBoolean("TicTacToe")) {
            plugin.getLogger().warning("Config key 'TicTacToe' is missing or not a boolean.")
            valid = false
        }


        // HAS
        if (small == null || !small.isBoolean("is-enabled") || !small.isInt("slot") || !(small.getInt("slot") >= 1 && small.getInt(
                "slot"
            ) <= 9)
        ) {
            plugin.getLogger().warning("Config key 'small-modus' is missing or not properly set.")
            valid = false
        }
        if (!config.isInt("minPlayersPerHASGroup")) {
            plugin.getLogger().warning("Config key 'HideAndSeek' is missing or not a boolean.")
            valid = false
        }
        if (!config.isInt("maxPlayersPerHASGroup")) {
            plugin.getLogger().warning("Config key 'maxPlayersPerHASGroup' is missing or not an integer.")
            valid = false
        }
        if (!config.isInt("maxSeekersPerHASGroup")) {
            plugin.getLogger().warning("Config key 'maxSeekersPerHASGroup' is missing or not an integer.")
            valid = false
        }
        if (loc == null || !loc.isString("world") || !loc.isDouble("x") || !loc.isDouble("y") || !loc.isDouble("z")) {
            plugin.getLogger().warning("Config key 'spawn-location' is missing or not properly set.")
            valid = false
        }
        if (!config.isInt("timeAutoStartHASGroup")) {
            plugin.getLogger().warning("Config key 'timeAutoStartHASGroup' is missing or not an integer.")
            valid = false
        }
        if (!config.isInt("playTimeHAS")) {
            plugin.getLogger().warning("Config key 'playTimeHAS' is missing or not an integer.")
            valid = false
        }
        if (!config.isInt("hideTimeHAS")) {
            plugin.getLogger().warning("Config key 'hideTimeHAS' is missing or not an integer.")
            valid = false
        }
        if (!config.isInt("HASHints")) {
            plugin.getLogger().warning("Config key 'HASHints' is missing or not an integer.")
            valid = false
        }
        if (!config.isInt("HintTimeHAS")) {
            plugin.getLogger().warning("Config key 'HintTimeHAS' is missing or not an integer.")
            valid = false
        }

        // Checks if something is wrong with the config.
        if (!valid) {
            plugin.getLogger()
                .warning("\u26A0 Invalid config.yml detected! The config will be reset to default values and backed up. \u26A0")
            try {
                val backupDir = File(plugin.getDataFolder(), "CorruptedConfigs")

                if (!backupDir.exists()) backupDir.mkdirs()

                val timestamp = SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(Date())
                val zipFile = File(backupDir, "corrupted-config_" + timestamp + ".zip")

                ZipOutputStream(FileOutputStream(zipFile)).use { zipOut ->
                    zipOut.putNextEntry(ZipEntry("config.yml"))
                    Files.copy(configFile.toPath(), zipOut)
                    zipOut.closeEntry()
                }
                // Delete the Corrupted Config
                configFile.delete()

                // Create a new config with default values
                plugin.saveDefaultConfig()
                plugin.getLogger().warning("Config has been reset to default values and backed up successfully!")
            } catch (e: IOException) {
                plugin.getLogger().warning("\u26A0 No backup can be created: " + e.message)
            }
        }
        return valid
    }
}
