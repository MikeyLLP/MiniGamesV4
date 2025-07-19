package de.mikeyllp.miniGamesV4.commands.admincommands

import de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands.SetEnableDisableGame
import de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands.SetHASSpawn
import de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands.SetNumber
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class SetCommand(commandName: String, plugin: JavaPlugin) : CommandAPICommand(commandName) {
    init {
        withSubcommand(SetNumber("setNum", plugin))
        withSubcommand(SetHASSpawn("HASSpawn", plugin))
        withSubcommand(SetEnableDisableGame("invert", plugin))

        executes((CommandExecutor { sender: CommandSender, args: CommandArguments ->
            if (!sender.hasPermission("minigamesv4.admin")) {
                MessageUtils.sendNoPermissionMessage(sender)
                return@CommandExecutor
            }
            MessageUtils.needHelpMessage(sender)
        }))
    }
}
