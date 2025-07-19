package de.mikeyllp.miniGamesV4.storage

import de.mikeyllp.miniGamesV4.MiniGamesV4
import de.mikeyllp.miniGamesV4.utils.ClickInviteUtils
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class ClickInviteStorage : Listener {
    // Checks if the Player is in the enable Listener Map and if he is it will remove him from the Map and perform the command
    @EventHandler
    fun onPlayerRightClick(event: PlayerInteractEntityEvent) {
        if (enableListener.containsKey(event.getPlayer())) {
            if (event.getRightClicked() is Player) {
                ClickInviteUtils.playerGotClicked(event.getPlayer(), event.getRightClicked() as Player)
            }
        }
    }

    @EventHandler
    fun onPlayerLeftClick(event: PrePlayerAttackEntityEvent) {
        if (enableListener.containsKey(event.getPlayer())) {
            if (event.getAttacked() is Player) {
                ClickInviteUtils.playerGotClicked(event.getPlayer(), event.getAttacked() as Player)
            }
        }
    }

    // Removes the Player from the enable Listener Map when he quits
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (enableListener.containsKey(event.getPlayer())) {
            ClickInviteUtils.removePlayer(event.getPlayer())
        }
    }

    companion object {
        val enableListener: MutableMap<Player?, BukkitTask?> = HashMap<Player?, BukkitTask?>()
        val whatGame: MutableMap<Player?, String?> = HashMap<Player?, String?>()

        // Adds the Player to the enable Listener Map and starts a 60 second timer after the timer ends the Player will be removed from the Map
        fun addEnableListener(player: Player, miniGame: String?) {
            // removes the Old listener if the player is already in the Map
            if (enableListener.containsKey(player)) {
                ClickInviteUtils.removePlayer(player)
                enableListener.put(player, object : BukkitRunnable() {
                    override fun run() {
                        ClickInviteUtils.removePlayer(player)
                        MessageUtils.sendMessage(player, "<red>Du kannst keine Spieler mehr einladen.</red>")
                    }
                }.runTaskLater(MiniGamesV4.Companion.getInstance(), 1200L))
                whatGame.put(player, miniGame)
                return
            }
            enableListener.put(player, object : BukkitRunnable() {
                override fun run() {
                    ClickInviteUtils.removePlayer(player)
                    MessageUtils.sendMessage(player, "<red>Du kannst keine Spieler mehr einladen.</red>")
                }
            }.runTaskLater(MiniGamesV4.Companion.getInstance(), 1200L))
            whatGame.put(player, miniGame)
        }
    }
}
