package de.mikeyllp.miniGamesV4.commands;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Player;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendHelpMessage;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;

public class HelpCommand extends CommandAPICommand {
    public HelpCommand(String commandName) {
        super(commandName);
        // Only an executor for the console
        executes(((sender, args) -> {
            //Checks if the player has permission to use this command
            if (!sender.hasPermission("minigamesv4.minigames")) {
                sendNoPermissionMessage((Player) sender);
                return;
            }
            //default commands
            sender.sendRichMessage("<gold>========== [<gradient:#00FF00:#007F00>MiniGames Help</gradient>] ==========</gold>");
            sender.sendRichMessage("<color:#00E5E5><> = Pflicht | [] = Optional</color>");
            sender.sendMessage("");
            sender.sendRichMessage("<color:#00FFD5>Allgemeine Befehle:");
            sendHelpMessage(sender, "/minigames help", "- Zeigt diese Hilfe");
            sendHelpMessage(sender, "/minigames <game> [<player>]", "- Spiele, die du spielen kannst");
            sendHelpMessage(sender, "/minigames accept <player>", "- Nimmt die Anfrage von dem Spieler an, den man angibt");
            sendHelpMessage(sender, "/minigames declined <player>", "- Lehnt die Anfrage von dem Spieler ab, den man angibt");
            sendHelpMessage(sender, "/minigames toggle <player>", "- Aktiviert/Deaktiviert, dass man Anfragen bekommt");
            sender.sendMessage("");
            sender.sendRichMessage("<color:#00FFD5>Spiele:");
            sender.sendMessage("");
            sender.sendRichMessage("TicTacToe");
            sender.sendRichMessage("Schere, Stein, Papier");

            // Here are the Admin Commands
            if (sender.isOp() || sender.hasPermission("MiniGamesV4.admin")) {
                sender.sendMessage("");
                sender.sendRichMessage("<color:#00FFD5>Admin Befehle:");
                sendHelpMessage(sender, "/minigames reload", "- Läd die Config neu");
            }
            sender.sendRichMessage("<gold>====================================</gold>");

        }));
    }
}
