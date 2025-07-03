package de.mikeyllp.miniGamesV4.commands.invites;

import de.mikeyllp.miniGamesV4.storage.ToggleInvitesStorage;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

import static de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.canInvitePlayer;
import static de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.gameInfo;
import static de.mikeyllp.miniGamesV4.utils.ClickInviteUtils.enableClickInvite;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.*;


public class InvitesTicTacToeGameCommand extends CommandAPICommand {
    public InvitesTicTacToeGameCommand(String commandName, JavaPlugin plugin, ToggleInvitesStorage storage) {
        super(commandName);
        // This creates a list of online players for tab completion. The "@" symbol is not allowed.
        withOptionalArguments(
                new StringArgument("player")
                        .replaceSuggestions(ArgumentSuggestions.stringCollection(info ->
                                Bukkit.getOnlinePlayers().stream()
                                        .map(Player::getName)
                                        .collect(Collectors.toList())
                        ))
        );
        // Sends an invite to the player to play TicTacToe.
        executesPlayer((sender, args) -> {

            // Checks if the player has permission to use this command.
            if (!sender.hasPermission("minigamesv4.minigames")) {
                sendNoPermissionMessage(sender);
                return;
            }

            // Checks if the TicTacToe is enabled in the config.
            boolean isEnabled = plugin.getConfig().getBoolean("TicTacToe");
            if (!isEnabled) {
                miniGamesDisabledMessage(sender);
                return;
            }

            // Enable Click Invite for Tic Tac Toe
            if (args.count() == 0) {
                enableClickInvite(sender, "TicTacToe");
                return;
            }

            Player targetPlayer = Bukkit.getPlayerExact(args.get(0).toString());

            if (gameInfo.containsKey(sender)) {
                sendAlreadyInGameMessage(sender);
                return;
            }

            // Checks if the target player is online
            if (targetPlayer == null) {
                sendNoOnlinePlayerMessage(sender);
                return;
            }


            canInvitePlayer(sender, targetPlayer, "TicTacToe", storage);

        });

    }
}