package de.mikeyllp.miniGamesV4.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public class MessageUtils {

    // To get the Config
    private static JavaPlugin plugin;

    private static FileConfiguration getLangConfig(String lang) {
        File file = new File(plugin.getDataFolder(), "languages/" + lang + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    private static FileConfiguration config() {
        return plugin.getConfig();
    }

    private static String PREFIX;

    public static void init(JavaPlugin pl) {
        plugin = pl;
        reloadConfig();
    }

    public static void reloadConfig() {
        plugin.reloadConfig();
        PREFIX = plugin.getConfig().getString("prefix", "").replace("%message%", "");
    }

    public static String prefix() {
        return PREFIX;
    }


    // You can use this very easily because you only set the message and the prefix are automatically added.
    public static void sendCustomMessage(CommandSender sender, String message) {
        sender.sendRichMessage(prefix() + message);
    }

    // The method for the standard warning messages.
    public static void sendCustomWarnMessage(CommandSender sender, String message) {
        sender.sendRichMessage(prefix() + "<red>" + message + "</red>");
    }

    // You can use this to send a help message with the command and description.
    public static void sendHelpMessage(CommandSender sender, String command, String description) {
        sender.sendRichMessage("<white>" + command + "</white> <gray>" + description);
    }

    // Normal messages


    // Info
    public static void sendNeedReloadMessage(CommandSender sender) {
        sendCustomMessage(sender, getLangConfig(config().getString("language")).getString("normal-message.info.reload"));
    }

    public static void sendGameSwitch(CommandSender sender, String game, boolean state) {
        if (state) {
            sendCustomMessage(sender, getLangConfig(config().getString("language")).getString("normal-message.info.enabled-game").replace("%game%", game));
            return;
        }
        sendCustomMessage(sender, getLangConfig(config().getString("language")).getString("normal-message.info.disabled-game").replace("%game%", game));
    }

    public static void needHelpMessage(CommandSender sender) {
        sendCustomMessage(sender, getLangConfig(config().getString("language")).getString("normal-message.info.need-help").replace("%command%", config().getString("command")));
    }

    // Error Messages
    public static void sendNoPermissionMessage(CommandSender sender) {
        sendCustomWarnMessage(sender, getLangConfig(config().getString("language")).getString("warning-message.no-permission"));
    }

    public static void miniGamesDisabledMessage(CommandSender sender) {
        sendCustomWarnMessage(sender, getLangConfig(config().getString("language")).getString("disabled-game"));
    }

    public static void sendAlreadyInGameMessage(CommandSender sender) {
        sendCustomWarnMessage(sender, getLangConfig(config().getString("language")).getString("already-in-game"));
    }

    public static void sendNoOnlinePlayerMessage(CommandSender sender) {
        sendCustomWarnMessage(sender, getLangConfig(config().getString("language")).getString("player-not-found"));
    }

    public static void sendNoInviteYourselfMessage(CommandSender sender) {
        sendCustomWarnMessage(sender, getLangConfig(config().getString("language")).getString("no-invite-yourself"));
    }
}
