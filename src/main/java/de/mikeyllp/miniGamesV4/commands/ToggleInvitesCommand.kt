package de.mikeyllp.miniGamesV4.commands

import de.mikeyllp.miniGamesV4.permission.MinigamesPermissionRegistry
import de.mikeyllp.miniGamesV4.storage
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand

fun CommandAPICommand.toggleInvitesCommand() = subcommand("toggle") {
    withPermission(MinigamesPermissionRegistry.COMMAND_TOGGLE)
    playerExecutor { player, args ->
        storage.addToggle(player)
    }
}
