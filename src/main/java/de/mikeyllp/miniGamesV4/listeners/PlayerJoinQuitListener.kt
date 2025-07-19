package de.mikeyllp.miniGamesV4.listeners

import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import de.mikeyllp.miniGamesV4.games.hideandseek.utils.removePlayersHideAndSeek
import de.mikeyllp.miniGamesV4.games.rps.RPSGame
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class PlayerJoinQuitListener(private val plugin: JavaPlugin) : Listener {
    //removes the player from the game when he leaves the server
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.getPlayer()

        // Check if the player was in RPS game
        if (RPSGame.Companion.inGameStatus.containsKey(player)) {
            MessageUtils.sendMessage(
                RPSGame.Companion.inGameStatus.get(player),
                "<red>" + player.getName() + " hat das Spiel verlassen!</red>"
            )
            RPSGame.Companion.removePlayersFromList(player, RPSGame.Companion.inGameStatus.get(player))
        }

        // Removes The Player from the listUntilX
        HideAndSeekGameGroups.Companion.listUntilX.removeIf { value: Player? -> value == player }
        removePlayersHideAndSeek.playerRemove(player, "disconnected", plugin)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.getPlayer()
        println(player.locale())

        if (!HideAndSeekGameGroups.Companion.gameState.isEmpty()) {
            for (entry in HideAndSeekGameGroups.Companion.gameGroup.entries) {
                val groupPlayers: MutableList<Player> = entry.value
                for (groupPlayer in groupPlayers) {
                    groupPlayer.hidePlayer(plugin, player)
                }
                for (outsider in Bukkit.getOnlinePlayers()) {
                    if (groupPlayers.contains(outsider)) continue
                    for (hidden in groupPlayers) {
                        outsider.hidePlayer(plugin, hidden)
                    }
                }
            }
        }
    }
}
