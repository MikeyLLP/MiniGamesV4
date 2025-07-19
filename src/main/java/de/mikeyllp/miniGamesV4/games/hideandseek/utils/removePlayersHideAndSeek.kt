package de.mikeyllp.miniGamesV4.games.hideandseek.utils

import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin


object removePlayersHideAndSeek {
    // Method to remove a player from the game group when he quit
    fun playerRemove(player: Player, reason: String, plugin: JavaPlugin): Boolean {
        // Check if the game group is not empty and iterate through the keys
        if (!HideAndSeekGameGroups.Companion.gameGroup.isEmpty()) {
            for (entry in HideAndSeekGameGroups.Companion.gameGroup.entries) {
                val groupName: String? = entry.key
                val groupPlayers: MutableList<Player> = entry.value
                if (groupPlayers.contains(player)) {
                    val config = plugin.getConfig()
                    val loc = Location(
                        plugin.getServer().getWorld(config.getString("spawn-location.world")!!),
                        config.getDouble("spawn-location.x"),
                        config.getDouble("spawn-location.y"),
                        config.getDouble("spawn-location.z")
                    )
                    player.setGlowing(false)
                    HideAndSeekGameGroups.Companion.hiddenNameTag.removePlayer(player)

                    when (reason) {
                        "gotFound" -> {
                            MessageUtils.sendMessage(player, "<red>Du wurdest gefunden!</red>")
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1f, 1.5f)
                            player.teleportAsync(loc)
                            player.setAllowFlight(true)
                            InvitePlayerStorage.gameInfo.remove(player)

                            // in whatever case the players who are not in the game will see the player again but the player who quit will see the gamers not anymore
                            for (outsider in Bukkit.getOnlinePlayers()) {
                                outsider.showPlayer(plugin, player)
                                player.showPlayer(plugin, outsider)
                            }
                            for (groupPlayer in groupPlayers) {
                                groupPlayer.hidePlayer(plugin, player)
                                player.hidePlayer(plugin, groupPlayer)
                            }


                            //removes the player from the gameGroup if he is a seeker
                            if (!groupPlayers.isEmpty()) {
                                // Removes the player safe from the List
                                groupPlayers.removeIf { value: Player? -> value == player }
                            }

                            for (p in groupPlayers) {
                                MessageUtils.sendMessage(
                                    p,
                                    "<gold>" + player.getName() + "</gold> wurde gefunden!"
                                )
                                p.playSound(player.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1f, 1.5f)
                            }
                        }

                        "quit" -> {
                            MessageUtils.sendMessage(player, "<red>Du hast das Spiel verlassen.</red>")
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1f, 1.5f)
                            player.teleportAsync(loc)
                            player.setAllowFlight(true)
                            InvitePlayerStorage.gameInfo.remove(player)

                            // in whatever case the players who are not in the game will see the player again but the player who quit will see the gamers not anymore
                            for (outsider in Bukkit.getOnlinePlayers()) {
                                outsider.showPlayer(plugin, player)
                                player.showPlayer(plugin, outsider)
                            }
                            for (groupPlayer in groupPlayers) {
                                groupPlayer.hidePlayer(plugin, player)
                                player.hidePlayer(plugin, groupPlayer)
                            }

                            seekerRemove(player, groupName, plugin)

                            //removes the player from the gameGroup if he is a seeker
                            if (!groupPlayers.isEmpty()) {
                                // Removes the player safe from the List
                                groupPlayers.removeIf { value: Player? -> value == player }
                            }
                        }

                        "disconnected" -> {
                            player.teleportAsync(loc)
                            player.setAllowFlight(true)
                            InvitePlayerStorage.gameInfo.remove(player)

                            // in whatever case the players who are not in the game will see the player again but the player who quit will see the gamers not anymore
                            for (outsider in Bukkit.getOnlinePlayers()) {
                                outsider.showPlayer(plugin, player)
                                player.showPlayer(plugin, outsider)
                            }
                            for (groupPlayer in groupPlayers) {
                                groupPlayer.hidePlayer(plugin, player)
                                player.hidePlayer(plugin, groupPlayer)
                            }

                            seekerRemove(player, groupName, plugin)

                            //removes the player from the gameGroup if he is a seeker
                            if (!groupPlayers.isEmpty()) {
                                // Removes the player safe from the List
                                groupPlayers.removeIf { value: Player? -> value == player }
                            }

                            for (p in groupPlayers) {
                                MessageUtils.sendMessage(
                                    p,
                                    "<gold>" + player.getName() + "<gold><red> hat das Spiel verlassen.</red>"
                                )
                            }
                        }

                        "gameEnd" ->                             // Teleport all players back to the spawn location
                            if (!groupPlayers.isEmpty()) {
                                for (p1 in Bukkit.getOnlinePlayers()) {
                                    for (p2 in Bukkit.getOnlinePlayers()) {
                                        if (p1 != p2) p1.showPlayer(plugin, p2)
                                    }
                                }

                                for (p in groupPlayers) {
                                    seekerRemove(p, groupName, plugin)
                                    p.teleportAsync(loc)
                                    p.setAllowFlight(true)
                                    p.setGlowing(false)
                                    InvitePlayerStorage.gameInfo.remove(p)
                                    HideAndSeekGameGroups.Companion.hiddenNameTag.removePlayer(p)
                                }
                                HideAndSeekGameGroups.Companion.gameGroup.remove(groupName)
                                HideAndSeekGameGroups.Companion.seekerGroup.remove(groupName)
                                HideAndSeekGameGroups.Companion.gameState.remove(groupName)
                            }
                    }
                    return true
                }
            }
        }
        return false
    }

    // removes the player from the seekerGroup if he is a seeker
    fun seekerRemove(player: Player, groupName: String?, plugin: JavaPlugin) {
        if (HideAndSeekGameGroups.Companion.seekerGroup.containsKey(groupName)) {
            HideAndSeekGameGroups.Companion.seekerGroup.get(groupName).remove(player)
            player.getAttribute(Attribute.SCALE)!!.setBaseValue(1.0)
            player.getInventory().setItem(plugin.getConfig().getInt("small-modus.slot") - 1, null)
            if (!HideAndSeekGameGroups.Companion.noMoveGroup.isEmpty()) {
                HideAndSeekGameGroups.Companion.noMoveGroup.get(groupName).remove(player)
            }
        }
    }
}
