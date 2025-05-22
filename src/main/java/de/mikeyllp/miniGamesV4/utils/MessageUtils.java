package de.mikeyllp.miniGamesV4.utils;

import org.bukkit.entity.Player;

public class MessageUtils {
    private static final String PREFIX = "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR><color:#00E5E5>";

    // Give the prefix Message when you call it.
    public static String prefix() {
        return PREFIX;
    }

    // Send the player a No Permission Message
    public static void sendNoPermissionMessage(Player sender) {
        sender.sendRichMessage(prefix() + "<red>Du hast keine Berechtigung, um diesen Befehl zu verwenden.</red>");
    }

    // Send the player a No Online Message
    public static void sendNoOnlinePlayerMessage(Player sender) {
        sender.sendRichMessage(prefix() + "<red>Der Spieler ist nicht online.</red>");
    }

    //Send the player a No Invite yourself Message
    public static void sendNoInviteYourselfMessage(Player sender) {
        sender.sendRichMessage(prefix() + "<red>Du kannst dich nicht selbst einladen.</red>");
    }

    // You can use this very easily because you only set the message and the prefix are automatically added.
    public static void sendCustomMessage(Player sender, String message) {
        sender.sendRichMessage(prefix() + message);
    }


}
