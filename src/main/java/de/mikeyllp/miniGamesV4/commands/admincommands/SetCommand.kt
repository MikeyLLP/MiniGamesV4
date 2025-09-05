package de.mikeyllp.miniGamesV4.commands.admincommands

import de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands.setEnableDisableGame
import de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands.setHASSpawnCommand
import de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands.setNumberCommand
import de.mikeyllp.miniGamesV4.messages.MessageUtils
import de.mikeyllp.miniGamesV4.permission.MinigamesPermissionRegistry
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand

fun CommandAPICommand.setCommand() = subcommand("set") {
    withPermission(MinigamesPermissionRegistry.COMMAND_SET)
    setNumberCommand()
    setHASSpawnCommand()
    setEnableDisableGame()

    anyExecutor { sender, args ->
        MessageUtils.needHelpMessage(sender)
    }
}