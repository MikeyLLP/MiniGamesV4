package de.mikeyllp.miniGamesV4.commands.invites;

import dev.jorel.commandapi.CommandAPICommand;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;

public class InvitesHideAndSeekGameCommand extends CommandAPICommand {
    public InvitesHideAndSeekGameCommand(String commandName) {
        super(commandName);
        // This command will add the player to a game list.
        executesPlayer((sender, args) -> {
            // Checks if the player has permission to use this command.
            if (!sender.hasPermission("minigamesv4.minigames")) {
                sendNoPermissionMessage(sender);
            }
        });
    }
}
