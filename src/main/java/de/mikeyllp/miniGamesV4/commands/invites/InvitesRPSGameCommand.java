package de.mikeyllp.miniGamesV4.commands.invites;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

import static de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.canInvitePlayer;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoOnlinePlayerMessage;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;


public class InvitesRPSGameCommand extends CommandAPICommand {
    public InvitesRPSGameCommand(String commandName) {
        super(commandName);

        // This creates a list of online players for tab completion. The "@" symbol is not allowed.
        withArguments(
                new StringArgument("player")
                        .replaceSuggestions(ArgumentSuggestions.stringCollection(info ->
                                Bukkit.getOnlinePlayers().stream()
                                        .map(Player::getName)
                                        .collect(Collectors.toList())
                        ))
        );
        // Sends an invite to the player to play Rock Paper Scissors.
        executesPlayer((sender, args) -> {
            Player targetPlayer = Bukkit.getPlayerExact(args.get(0).toString());

            // Checks if the player has permission to use this command.
            if (!sender.hasPermission("minigamesv4.minigames")) {
                sendNoPermissionMessage(sender);
                return;
            }

            // Checks if the target player is online.
            if (targetPlayer == null) {
                sendNoOnlinePlayerMessage(sender);
                return;
            }


            canInvitePlayer(sender, targetPlayer, "RPS");

        });
    }
}
