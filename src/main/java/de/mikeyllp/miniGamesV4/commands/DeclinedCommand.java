package de.mikeyllp.miniGamesV4.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

import static de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.removeInvite;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoOnlinePlayerMessage;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;

//adds all online players to the tab completer
public class DeclinedCommand extends CommandAPICommand {
    public DeclinedCommand(String commandName) {
        super(commandName);
        // This is a list that adds online players to the TabCompleter and you can´t use @
        withArguments(
                new StringArgument("player")
                        .replaceSuggestions(ArgumentSuggestions.stringCollection(info ->
                                Bukkit.getOnlinePlayers().stream()
                                        .map(Player::getName)
                                        .collect(Collectors.toList())
                        ))
        );
        executesPlayer((invited, args) -> {
            Player inviter = Bukkit.getPlayerExact(args.get(0).toString());

            // Checks if the player has permission to use this command
            if (!invited.hasPermission("minigamesv4.minigames")) {
                sendNoPermissionMessage(invited);
                return;
            }

            if (inviter == null) {
                sendNoOnlinePlayerMessage(invited);
                return;
            }

            // Remove this player from the invite list
            removeInvite(inviter, invited);
        });
    }
}
