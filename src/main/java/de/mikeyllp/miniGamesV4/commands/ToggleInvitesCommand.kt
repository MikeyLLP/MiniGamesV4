package de.mikeyllp.miniGamesV4.commands

import de.mikeyllp.miniGamesV4.storage.ToggleInvitesStorage
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import org.bukkit.entity.Player

class ToggleInvitesCommand(commandName: String, storage: ToggleInvitesStorage) : CommandAPICommand(commandName) {
    init {
        //A Command witch toggle that he can get Invited
        executesPlayer((PlayerCommandExecutor { sender: Player?, args: CommandArguments? ->
            //Checks if the player has permission to use this command
            if (!sender!!.hasPermission("minigamesv4.minigames")) {
                MessageUtils.sendNoPermissionMessage(sender)
                return@PlayerCommandExecutor
            }
            storage.addToggle(sender)
        }))
    }
}
