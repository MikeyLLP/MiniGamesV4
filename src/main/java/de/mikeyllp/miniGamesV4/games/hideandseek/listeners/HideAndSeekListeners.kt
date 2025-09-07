package de.mikeyllp.miniGamesV4.games.hideandseek.listeners

import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import de.mikeyllp.miniGamesV4.games.hideandseek.utils.RemovePlayersHideAndSeek
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent

class HideAndSeekListeners() : Listener {

    @EventHandler
    fun onClickEvent(event: PrePlayerAttackEntityEvent) {
        if (event.attacked is Player) {

            val player = event.player
            event.isCancelled

            for (entry in HideAndSeekGameGroups.Companion.noMoveGroup.entries) {
                val seekerList: MutableList<Player> = entry.value
                if (seekerList.contains(player)) {
                    return
                }
            }

            for (entry in HideAndSeekGameGroups.seekerGroup.entries) {
                val groupName: String = entry.key
                val seekerList: MutableList<Player> = entry.value

                if (seekerList.contains(player)) {
                    HideAndSeekGameGroups.gameGroup[groupName]?.let { playerList ->
                        val clickedPlayer = event.attacked as Player
                        if (playerList.contains(clickedPlayer)) {
                            RemovePlayersHideAndSeek.playerRemove(clickedPlayer, "gotFound")
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.getEntity() !is Player) return
        val player = event.entity as Player

        for (group in HideAndSeekGameGroups.gameGroup.values) {
            if (group.contains(player)) {
                event.isCancelled = true
                return
            }
        }
    }

    @EventHandler
    fun onHunger(event: FoodLevelChangeEvent) {
        if (event.entity !is Player) return
        val player = event.entity as Player

        for (group in HideAndSeekGameGroups.gameGroup.values) {
            if (group.contains(player)) {
                event.isCancelled = true
                return
            }
        }
    }
}
