package de.mikeyllp.miniGamesV4.commands.admincommands

import de.mikeyllp.miniGamesV4.utils.MessageUtils
import de.mikeyllp.miniGamesV4.utils.clearUtils
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class ReloadConfigCommand(commandName: String, plugin: JavaPlugin) : CommandAPICommand(commandName) {
    init {
        executes(CommandExecutor { sender: CommandSender, args: CommandArguments ->
            // Checks if the player has permission to use this command
            if (!sender.hasPermission("minigamesv4.admin")) {
                MessageUtils.sendNoPermissionMessage(sender)
                return@CommandExecutor
            }
            reloadConfig(sender, plugin)
        })
    }

    companion object {
        fun reloadConfig(sender: CommandSender, plugin: JavaPlugin) {
            clearUtils.clearAllLists(sender, plugin)
        }
    }
}
