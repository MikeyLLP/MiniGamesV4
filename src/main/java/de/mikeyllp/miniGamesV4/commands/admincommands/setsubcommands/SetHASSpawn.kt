package de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands

import de.mikeyllp.miniGamesV4.messages.MessageUtils
import de.mikeyllp.miniGamesV4.permission.MinigamesPermissionRegistry.COMMAND_SET
import de.mikeyllp.miniGamesV4.plugin
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.LocationArgument
import dev.jorel.commandapi.arguments.WorldArgument
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import org.bukkit.Location
import org.bukkit.World

fun CommandAPICommand.setHASSpawnCommand() = subcommand("HASSpawn") {
    withPermission(COMMAND_SET)
    withArguments(LocationArgument("location"))
    withArguments(WorldArgument("world"))

    anyExecutor { sender, args ->
        val config = plugin.getConfig()

        val worldArg = args.get("world") as World
        val locationArg = args.get("location") as Location

        val locX = locationArg.x
        val locY = locationArg.y
        val locZ = locationArg.z

        config.set("spawn-location.world", worldArg.name)
        config.set("spawn-location.x", locX)
        config.set("spawn-location.y", locY)
        config.set("spawn-location.z", locZ)


        plugin.saveConfig()
        MessageUtils.sendNeedReloadMessage(sender)
    }
}

