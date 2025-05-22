package de.mikeyllp.miniGamesV4.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

import static de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.canGameStart;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoOnlinePlayerMessage;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;


public class AcceptCommand extends CommandAPICommand {
    public AcceptCommand(String commandName) {
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

            if (invited.toString().equals(inviter)) {

            }

            // Checks if the player has permission to use this command
            if (!invited.hasPermission("minigamesv4.minigames")) {
                sendNoPermissionMessage(invited);
                return;
            }

            if (inviter == null) {
                sendNoOnlinePlayerMessage(invited);
                return;
            }

            // Checks if the player can play
            canGameStart(inviter, invited);

        });
    }
}
