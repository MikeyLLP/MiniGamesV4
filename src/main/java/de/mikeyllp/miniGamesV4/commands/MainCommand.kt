package de.mikeyllp.miniGamesV4.commands

import de.mikeyllp.miniGamesV4.commands.admincommands.clearCommand
import de.mikeyllp.miniGamesV4.commands.admincommands.reloadConfigCommand
import de.mikeyllp.miniGamesV4.commands.admincommands.setCommand
import de.mikeyllp.miniGamesV4.gui.MenuMain
import de.mikeyllp.miniGamesV4.permission.MinigamesPermissionRegistry
import de.mikeyllp.miniGamesV4.plugin
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor

fun mainCommand() = commandAPICommand(plugin.config.getString("command").toString()) {
    withPermission(MinigamesPermissionRegistry.COMMAND_MINIGAMES_MENU)


    reloadConfigCommand()
    setCommand()


    acceptCommand()
    clearCommand()
    declineCommand()
    toggleInvitesCommand()
    quitCommand()
    helpCommand()

    playerExecutor { player, args ->
        MenuMain.openGameMenue(player)
    }
}
