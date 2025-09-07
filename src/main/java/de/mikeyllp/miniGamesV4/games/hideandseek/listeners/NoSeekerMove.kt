package de.mikeyllp.miniGamesV4.games.hideandseek.listeners

import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class NoSeekerMove : Listener {
    @EventHandler
    fun onSeekerMove(event: PlayerMoveEvent) {
        for (entry in HideAndSeekGameGroups.noMoveGroup.entries) {
            val seekerList: MutableList<Player> = entry.value
            if (seekerList.contains(event.player)) {
                event.isCancelled = true
            }
        }
    }
}
