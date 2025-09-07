package de.mikeyllp.miniGamesV4.games.hideandseek

import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import de.mikeyllp.miniGamesV4.games.hideandseek.utils.WaitingForPlayersUtils
import de.mikeyllp.miniGamesV4.messages.MessageUtils
import de.mikeyllp.miniGamesV4.messages.Translator
import de.mikeyllp.miniGamesV4.plugin
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage
import org.bukkit.Location
import org.bukkit.entity.Player

object HideAndSeekGame {
    fun addPlayerToHAS(player: Player) {
        if (HideAndSeekGameGroups.listUntilX.contains(player)) {
            MessageUtils.sendMessage(
                player, Translator.translatable(
                    "warning.message.already-in-queue"
                )
            )
            return
        }

        val uuid = player.uniqueId

        if (InvitePlayerStorage.runningGames.containsKey(uuid)) {
            MessageUtils.sendMessage(
                player,
                Translator.translatable("warning-message.already-in-game.self")
            )
            return
        }



        HideAndSeekGameGroups.listUntilX.add(player)
        InvitePlayerStorage.runningGames[uuid] = uuid
        if (WaitingForPlayersUtils.waitingTask == null) {
            WaitingForPlayersUtils.startWaitingTask()
        }
    }


    fun startGame() {
        val config = plugin.config

        val loc = Location(
            plugin.server.getWorld(config.getString("spawn-location.world")!!),
            config.getDouble("spawn-location.x"),
            config.getDouble("spawn-location.y"),
            config.getDouble("spawn-location.z")
        )

        for (p in HideAndSeekGameGroups.Companion.listUntilX) {
            p.teleportAsync(loc)
        }

        HideAndSeekGameGroups.createGroupFromHAS(HideAndSeekGameGroups.listUntilX.size)
    }
}
