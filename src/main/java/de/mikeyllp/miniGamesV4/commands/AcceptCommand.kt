package de.mikeyllp.miniGamesV4.commands

import de.mikeyllp.miniGamesV4.permission.MinigamesPermissionRegistry
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import org.bukkit.entity.Player

fun CommandAPICommand.acceptCommand() = subcommand("accept") {
    withPermission(MinigamesPermissionRegistry.COMMAND_ACCEPT)
    playerArgument("toAccept")

    playerExecutor { player, args ->
        val toAccept: Player by args

        if (player == toAccept) {
            MessageUtils.sendMessage(player, "warning-message.no-invite-yourself")
            return@playerExecutor
        }

        InvitePlayerStorage.canGameStart(toAccept, player)
    }
}