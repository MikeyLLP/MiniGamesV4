package de.mikeyllp.miniGamesV4.utils;

import org.bukkit.command.CommandSender;

public class MessageUtils {
    private static final String PREFIX = "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR><color:#00E5E5>";

    // Give the prefix Message when you call it.
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
