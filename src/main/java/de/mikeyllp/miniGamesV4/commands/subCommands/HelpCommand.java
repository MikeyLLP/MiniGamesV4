package de.mikeyllp.miniGamesV4.commands.subCommands;

import dev.jorel.commandapi.CommandAPICommand;

public class HelpCommand extends CommandAPICommand {
    public HelpCommand(String commandName) {
        super(commandName);
        // Only a executor for the console
        executes(((sender, args) -> {
            //Checks if the player has permission to use this command
            if (!sender.hasPermission("minigamesv4.minigames")) {
                String prefix = "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR>";
                sender.sendRichMessage(prefix + "<red>Du hast keine Berechtigung, um diesen Command zu nutzen.</red>");
                return;
            }
            //default commands
            sender.sendRichMessage("<gold>========== [<gradient:#00FF00:#007F00>MiniGames Help</gradient>] ==========</gold>");
            sender.sendRichMessage("<color:#00E5E5><> = Pflicht | [] = Optional</color>");
            sender.sendMessage("");
            sender.sendRichMessage("<color:#00FFD5>Allgemeine Befehle:");
            sender.sendRichMessage("<white>/minigames help </white><gray>- Zeigt diese Hilfe</gray>");
            sender.sendRichMessage("<white>/minigames accept <player> </white><gray>- Nimmt die Anfrage von dem Spieler an, den man angibt</gray>");
            sender.sendRichMessage("<white>/minigames declined <player> </white><gray>- Lehnt die Anfrage von dem Spieler ab, den man angibt</gray>");
            sender.sendRichMessage("<white>/minigames toggle <player> </white><gray>- Aktiviert/Deaktiviert, dass man Anfragen bekommt</gray>");
            sender.sendRichMessage("<white>/minigames <game> <player> </white><gray>- Spiele, die du spielen kannst</gray>");
            sender.sendMessage("");
            sender.sendRichMessage("<color:#00FFD5>Spiele:");
            sender.sendMessage("");
            sender.sendRichMessage("TicTacToe");
            sender.sendRichMessage("Schere, Stein, Papier");
            // Here are the Admin Commands
            sender.sendMessage("");
            if (sender.isOp() || sender.hasPermission("MiniGamesV4.admin")) {
                sender.sendRichMessage("<color:#00FFD5>Admin Befehle:");
                sender.sendRichMessage("\u2728 Comming Soon \u2728");
            }
            sender.sendRichMessage("<gold>====================================</gold>");

        }));
    }
}
