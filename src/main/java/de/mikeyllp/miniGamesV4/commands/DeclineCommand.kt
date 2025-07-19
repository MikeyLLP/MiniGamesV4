package de.mikeyllp.miniGamesV4.commands

import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import de.mikeyllp.miniGamesV4.utils.MinigamesPermissionRegistry
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import org.bukkit.entity.Player

//adds all online players to the tab completer

fun CommandAPICommand.declineCommand() = subcommand("decline") {
    withPermission(MinigamesPermissionRegistry.COMMAND_DECLINE)
    playerArgument("toDecline")

    playerExecutor { player, args ->
        val toDecline: Player by args

        if (player == toDecline) {
            MessageUtils.sendMessage(player, "warning-message.no-invite-yourself")
            return@playerExecutor
        }

        InvitePlayerStorage.removeInvite(toDecline, player)
    }
}

