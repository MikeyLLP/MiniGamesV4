package de.mikeyllp.miniGamesV4.commands.admincommands

import de.mikeyllp.miniGamesV4.utils.MessageUtils
import de.mikeyllp.miniGamesV4.utils.MinigamesPermissionRegistry
import de.mikeyllp.miniGamesV4.utils.clearUtils
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand

fun CommandAPICommand.clearCommand() = subcommand("clear") {
    anyExecutor { sender, args ->
        if (!sender.hasPermission(MinigamesPermissionRegistry.COMMAND_CLEAR)) {
            MessageUtils.sendNoPermissionMessage(sender)
            return@anyExecutor
        }

        clearUtils.clearAllLists(sender)
    }
}