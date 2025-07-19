package de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands

import de.mikeyllp.miniGamesV4.utils.MessageUtils
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.LocationArgument
import dev.jorel.commandapi.arguments.WorldArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class SetHASSpawn(commandName: String, plugin: JavaPlugin) : CommandAPICommand(commandName) {
    init {
        withArguments(LocationArgument("location"))
        withArguments(WorldArgument("world"))
        executes(CommandExecutor { sender: CommandSender, args: CommandArguments ->
            // Checks if the player has permission to use this command
            if (!sender.hasPermission("minigamesv4.admin")) {
                MessageUtils.sendNoPermissionMessage(sender)
                return@CommandExecutor
            }
            // Get the Config
            val config = plugin.getConfig()

            val worldArg = args.get("world") as World?
            val locationArg = args.get("location") as Location?


            val locX = locationArg!!.x
            val locY = locationArg.y
            val locZ = locationArg.z

            // Set the spawn location in the config
            config.set("spawn-location.world", worldArg?.name)
            config.set("spawn-location.x", locX)
            config.set("spawn-location.y", locY)
            config.set("spawn-location.z", locZ)

            // Save the config
            plugin.saveConfig()
            MessageUtils.sendNeedReloadMessage(sender)
        })
    }
}

