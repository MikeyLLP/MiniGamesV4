package de.mikeyllp.miniGamesV4.games.hideandseek.utils

import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import de.mikeyllp.miniGamesV4.messages.MessageUtils
import de.mikeyllp.miniGamesV4.messages.Translator
import de.mikeyllp.miniGamesV4.plugin
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player


object RemovePlayersHideAndSeek {

    fun playerRemove(player: Player, reason: String): Boolean {
        if (!HideAndSeekGameGroups.gameGroup.isEmpty()) {
            for (entry in HideAndSeekGameGroups.gameGroup.entries) {
                val groupName: String = entry.key
                val groupPlayers: MutableList<Player> = entry.value

                if (groupPlayers.contains(player)) {
                    val config = plugin.config
                    val uuid = player.uniqueId
                    val loc = Location(
                        plugin.server.getWorld(config.getString("spawn-location.world")!!),
                        config.getDouble("spawn-location.x"),
                        config.getDouble("spawn-location.y"),
                        config.getDouble("spawn-location.z")
                    )
                    player.isGlowing = true
                    HideAndSeekGameGroups.hiddenNameTag?.removePlayer(player)

                    when (reason) {
                        "gotFound" -> {
                            MessageUtils.sendMessage(
                                player,
                                Translator.translatable("has.message.you-got-found")
                            )

                            player.playSound(player.location, Sound.ENTITY_PLAYER_DEATH, 1f, 1.5f)
                            player.teleportAsync(loc)
                            player.allowFlight = true

                            InvitePlayerStorage.runningGames.remove(uuid)


                            for (outsider in Bukkit.getOnlinePlayers()) {
                                outsider.showPlayer(plugin, player)
                                player.showPlayer(plugin, outsider)
                            }

                            for (groupPlayer in groupPlayers) {
                                groupPlayer.hidePlayer(plugin, player)
                                player.hidePlayer(plugin, groupPlayer)
                            }

                            if (!groupPlayers.isEmpty()) {
                                groupPlayers.removeIf { value: Player? -> value == player }
                            }

                            for (p in groupPlayers) {
                                MessageUtils.sendMessage(
                                    p,
                                    Translator.translatable("has.message.player-found", player.name)
                                )

                                p.playSound(player.location, Sound.ENTITY_PLAYER_DEATH, 1f, 1.5f)
                            }
                        }

                        "quit" -> {
                            MessageUtils.sendMessage(
                                player,
                                Translator.translatable("has.message.you-left-game")
                            )

                            player.playSound(player.location, Sound.ENTITY_PLAYER_DEATH, 1f, 1.5f)
                            player.teleportAsync(loc)
                            player.allowFlight = true
                            InvitePlayerStorage.runningGames.remove(uuid)

                            for (outsider in Bukkit.getOnlinePlayers()) {
                                outsider.showPlayer(plugin, player)
                                player.showPlayer(plugin, outsider)
                            }

                            for (groupPlayer in groupPlayers) {
                                groupPlayer.hidePlayer(plugin, player)
                                player.hidePlayer(plugin, groupPlayer)
                            }

                            seekerRemove(player, groupName)

                            if (!groupPlayers.isEmpty()) {
                                groupPlayers.removeIf { value: Player -> value == player }
                            }
                        }

                        "disconnected" -> {
                            player.teleportAsync(loc)
                            player.allowFlight = true
                            InvitePlayerStorage.runningGames.remove(uuid)

                            for (outsider in Bukkit.getOnlinePlayers()) {
                                outsider.showPlayer(plugin, player)
                                player.showPlayer(plugin, outsider)
                            }
                            for (groupPlayer in groupPlayers) {
                                groupPlayer.hidePlayer(plugin, player)
                                player.hidePlayer(plugin, groupPlayer)
                            }

                            seekerRemove(player, groupName)

                            if (!groupPlayers.isEmpty()) {
                                groupPlayers.removeIf { value: Player -> value == player }
                            }

                            for (p in groupPlayers) {
                                MessageUtils.sendMessage(
                                    p,
                                    Translator.translatable("has.message.player-left-game", player.name)
                                )
                            }
                        }

                        "gameEnd" ->
                            if (!groupPlayers.isEmpty()) {
                                for (p1 in Bukkit.getOnlinePlayers()) {
                                    for (p2 in Bukkit.getOnlinePlayers()) {
                                        if (p1 != p2) p1.showPlayer(plugin, p2)
                                    }
                                }

                                for (p in groupPlayers) {
                                    val uuid = p.uniqueId
                                    seekerRemove(p, groupName)
                                    p.teleportAsync(loc)
                                    p.allowFlight = true
                                    p.isGlowing = false
                                    InvitePlayerStorage.runningGames.remove(uuid)
                                    HideAndSeekGameGroups.hiddenNameTag?.removePlayer(p)
                                }
                                HideAndSeekGameGroups.gameGroup.remove(groupName)
                                HideAndSeekGameGroups.seekerGroup.remove(groupName)
                                HideAndSeekGameGroups.gameState.remove(groupName)
                            }
                    }
                    return true
                }
            }
        }
        return false
    }

    fun seekerRemove(player: Player, groupName: String) {
        if (HideAndSeekGameGroups.seekerGroup.containsKey(groupName)) {
            HideAndSeekGameGroups.seekerGroup[groupName]?.remove(player)

            player.getAttribute(Attribute.SCALE)?.baseValue = 1.0
            player.inventory.setItem(plugin.config.getInt("small-modus.slot") - 1, null)

            if (!HideAndSeekGameGroups.noMoveGroup.isEmpty()) {
                HideAndSeekGameGroups.noMoveGroup[groupName]?.remove(player)
            }
        }
    }
}
