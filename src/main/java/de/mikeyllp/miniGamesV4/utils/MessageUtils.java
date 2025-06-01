package de.mikeyllp.miniGamesV4.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;


public class MessageUtils {

    // To get the Config
    private static JavaPlugin plugin;
    private static String PREFIX;

    public static void init(JavaPlugin pl) {
        plugin = pl;
        reloadConfig();
    }

    public static void reloadConfig() {
        plugin.reloadConfig();
        PREFIX = plugin.getConfig().getString("prefix", "").replace("{message}", "");
    }

    public static String prefix() {
        return PREFIX;
    }

    // Send the player a No Permission Message
    public static void sendNoPermissionMessage(CommandSender sender) {
        sendCustomMessage(sender, "<red>Du hast keine Berechtigung, um diesen Befehl zu verwenden.</red>");
    }

    // Send the player a Arleady in Game Message
    public static void sendAlreadyInGameMessage(CommandSender sender) {
        sendCustomMessage(sender, "<red>Du bist bereits in einem Spiel.</red>");
    }

    // Send the player a No Online Message
    public static void sendNoOnlinePlayerMessage(CommandSender sender) {
        sendCustomMessage(sender, "<red>Der Spieler ist nicht online.</red>");
    }

    //Send the player a No Invite yourself Message
    public static void sendNoInviteYourselfMessage(CommandSender sender) {
        sendCustomMessage(sender, "<red>Du kannst dich nicht selbst einladen.</red>");
    }

    public static void miniGamesDisabledMessage(CommandSender sender) {
        sendCustomMessage(sender, "<red>Dieses MiniGame ist derzeit deaktiviert.</red>");
    }

    // You can use this very easily because you only set the message and the prefix are automatically added.
    public static void sendCustomMessage(CommandSender sender, String message) {
        sender.sendRichMessage(prefix() + message);
    }

    // You can use this to send a help message with the command and description.
    public static void sendHelpMessage(CommandSender sender, String command, String description) {
        sender.sendRichMessage("<white>" + command + "</white> <gray>" + description);
    }
}
