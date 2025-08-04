package de.mikeyllp.miniGamesV4.commands.admincommands

import de.mikeyllp.miniGamesV4.permission.MinigamesPermissionRegistry
import de.mikeyllp.miniGamesV4.utils.ClearUtils
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand

fun CommandAPICommand.reloadConfigCommand() = subcommand("reload") {
    withPermission(MinigamesPermissionRegistry.COMMAND_CLEAR)
    anyExecutor { sender, args ->
        ClearUtils.clearAllLists(sender)
    }
}
