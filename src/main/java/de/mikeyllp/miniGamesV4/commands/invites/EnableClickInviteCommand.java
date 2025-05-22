package de.mikeyllp.miniGamesV4.commands.invites;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;

import static de.mikeyllp.miniGamesV4.utils.ClickInviteUtils.enableClickInvite;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;

public class EnableClickInviteCommand extends CommandAPICommand {

    public EnableClickInviteCommand(String commandName) {
        super(commandName);
        withArguments(new MultiLiteralArgument("minigame", "RPS", "TicTacToe"));
        executesPlayer((sender, args) -> {
            // Checks if the player has permission to use this command.
            if (!sender.hasPermission("minigamesv4.minigames")) {
                sendNoPermissionMessage(sender);
                return;
            }
            //Checks wich minigame the player wants to play
            switch ((String) args.get("minigame")) {
                case "TicTacToe" -> {
                    // Enable Click Invite for Tic Tac Toe
                    enableClickInvite(sender, "TicTacToe");
                }
                case "RPS" -> {
                    // Enable Click Invite for Rock Paper Scissors
                    enableClickInvite(sender, "RPS");
                }
            }
        });
    }
}
