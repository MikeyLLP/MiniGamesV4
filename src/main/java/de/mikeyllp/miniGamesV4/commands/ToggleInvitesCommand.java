package de.mikeyllp.miniGamesV4.commands;

import de.mikeyllp.miniGamesV4.database.Database;
import de.mikeyllp.miniGamesV4.storage.ToggleInvitesStorage;
import dev.jorel.commandapi.CommandAPICommand;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;

public class ToggleInvitesCommand extends CommandAPICommand {


    public ToggleInvitesCommand(String commandName, ToggleInvitesStorage storage) {
        super(commandName);
        //A Command witch toggle that he can get Invited
        executesPlayer(((sender, args) -> {
            //Checks if the player has permission to use this command
            if (!sender.hasPermission("minigamesv4.minigames")) {
                sendNoPermissionMessage(sender);
                return;
            }
            storage.addToggle(sender);
        }));
    }
}
