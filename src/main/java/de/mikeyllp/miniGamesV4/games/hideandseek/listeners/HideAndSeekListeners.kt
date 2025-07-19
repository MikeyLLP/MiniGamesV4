package de.mikeyllp.miniGamesV4.games.hideandseek.listeners

import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import de.mikeyllp.miniGamesV4.games.hideandseek.utils.removePlayersHideAndSeek
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.plugin.java.JavaPlugin

class HideAndSeekListeners(private val plugin: JavaPlugin?) : Listener {
    // For the Hide and Seek game to
    @EventHandler
    fun onClickEvent(event: PrePlayerAttackEntityEvent) {
        if (event.getAttacked() is Player) {
            val player = event.getPlayer()
            event.setCancelled(true)
            // So that the seekers cannot direktly click on the players to remove them
            for (entry in HideAndSeekGameGroups.Companion.noMoveGroup.entries) {
                val seekerList: MutableList<Player?> = entry.value
                if (seekerList.contains(player)) {
                    return
                }
            }

            // We chack all the seeker groups if the player is a seeker
            for (entry in HideAndSeekGameGroups.Companion.seekerGroup.entries) {
                val groupName: String? = entry.key
                val seekerList: MutableList<Player?> = entry.value
                // Check if the player is a seeker
                if (seekerList.contains(player)) {
                    val playerList: MutableList<Player?>? = HideAndSeekGameGroups.Companion.gameGroup.get(groupName)
                    // Get the player who was clicked
                    val clickedPlayer = event.getAttacked() as Player
                    if (playerList != null && playerList.contains(clickedPlayer)) {
                        removePlayersHideAndSeek.playerRemove(clickedPlayer, "gotFound", plugin)
                    }
                }
            }
        }
    }

    // That the players cannot take damage
    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.getEntity() !is Player) return
        for (group in HideAndSeekGameGroups.Companion.gameGroup.values) {
            if (group.contains(player)) {
                event.setCancelled(true)
                return
            }
        }
    }

    // That the players cannot lose hunger
    @EventHandler
    fun onHunger(event: FoodLevelChangeEvent) {
        if (event.getEntity() !is Player) return
        for (group in HideAndSeekGameGroups.Companion.gameGroup.values) {
            if (group.contains(player)) {
                event.setCancelled(true)
                return
            }
        }
    }
}
