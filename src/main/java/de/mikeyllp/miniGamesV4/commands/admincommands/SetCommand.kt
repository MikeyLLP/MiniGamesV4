package de.mikeyllp.miniGamesV4.commands.admincommands

import de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands.SetEnableDisableGame
import de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands.SetHASSpawn
import de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands.setNumberCommand
import de.mikeyllp.miniGamesV4.permission.MinigamesPermissionRegistry
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand

fun CommandAPICommand.setCommand() = subcommand("set") {
    withPermission(MinigamesPermissionRegistry.COMMAND_SET)
    setNumberCommand()
    withSubcommand(SetHASSpawn("HASSpawn"))
    withSubcommand(SetEnableDisableGame("invert"))

    anyExecutor { sender, args ->
        MessageUtils.needHelpMessage(sender)
    }
}