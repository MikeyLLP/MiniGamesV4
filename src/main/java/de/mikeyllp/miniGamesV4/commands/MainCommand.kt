package de.mikeyllp.miniGamesV4.commands

import de.mikeyllp.miniGamesV4.commands.admincommands.ReloadConfigCommand
import de.mikeyllp.miniGamesV4.commands.admincommands.SetCommand
import de.mikeyllp.miniGamesV4.commands.admincommands.clearCommand
import de.mikeyllp.miniGamesV4.gui.MenuMain
import de.mikeyllp.miniGamesV4.plugin
import de.mikeyllp.miniGamesV4.utils.MinigamesPermissionRegistry
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor

fun mainCommand() = commandAPICommand(plugin.config.getString("command").toString()) {
    withPermission(MinigamesPermissionRegistry.COMMAND_MINIGAMES_MENU)

    val storage = plugin.toggleInvitesStorage

    // Admin Commands
    withSubcommand(ReloadConfigCommand("reload", plugin))
    withSubcommand(SetCommand("set", plugin))

    // Normal Commands
    acceptCommand()
    clearCommand()
    declineCommand()
    withSubcommand(ToggleInvitesCommand("toggle", storage))
    withSubcommand(QuitCommand("quit", plugin))
    helpCommand()

    playerExecutor { player, args ->
        MenuMain.openGameMenue(player)
    }
}
