package de.mikeyllp.miniGamesV4.games.hideandseek

import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import de.mikeyllp.miniGamesV4.games.hideandseek.utils.WaitingForPlayersUtils
import de.mikeyllp.miniGamesV4.plugin
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import org.bukkit.Location
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

object HideAndSeekGame {
    fun addPlayerToHAS(player: Player) {
        if (HideAndSeekGameGroups.Companion.listUntilX.contains(player)) {
            MessageUtils.sendMessage(player, "<red>Du bist bereits in der Warteschlange!")
            return
        }

        if (InvitePlayerStorage.gameInfo.containsKey(player)) {
            MessageUtils.sendAlreadyInGameMessage(player)
            return
        }

        // Add the player to the list so that they can´t be invited to another game
        HideAndSeekGameGroups.Companion.listUntilX.add(player)
        InvitePlayerStorage.gameInfo.put(player, player)
        if (WaitingForPlayersUtils.waitingTask == null) {
            WaitingForPlayersUtils.startWaitingTask(plugin)
        }
    }


    fun startGame(plugin: JavaPlugin, config: FileConfiguration) {
        val loc = Location(
            plugin.getServer().getWorld(config.getString("spawn-location.world")!!),
            config.getDouble("spawn-location.x"),
            config.getDouble("spawn-location.y"),
            config.getDouble("spawn-location.z")
        )

        for (p in HideAndSeekGameGroups.Companion.listUntilX) {
            p.teleportAsync(loc)
        }

        HideAndSeekGameGroups.Companion.createGroupFromHAS(HideAndSeekGameGroups.Companion.listUntilX.size, plugin)
    }
}
