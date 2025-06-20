package de.mikeyllp.miniGamesV4.games.hideandseek.utils;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

import static de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups.*;
import static de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.gameInfo;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class removePlayersHideAndSeek {

    // Method to remove a player from the game group when he quit
    public static boolean playerRemove(Player player, String reason, JavaPlugin plugin) {
        // Check if the game group is not empty and iterate through the keys
        if (!gameGroup.isEmpty()) {
            for (Map.Entry<String, List<Player>> entry : gameGroup.entrySet()) {
                String groupName = entry.getKey();
                List<Player> groupPlayers = entry.getValue();
                if (groupPlayers.contains(player)) {
                    FileConfiguration config = plugin.getConfig();
                    Location loc = new Location(
                            plugin.getServer().getWorld(config.getString("spawn-location.world")),
                            config.getDouble("spawn-location.x"),
                            config.getDouble("spawn-location.y"),
                            config.getDouble("spawn-location.z")
                    );
                    player.setGlowing(false);
                    hiddenNameTag.removePlayer(player);

                    switch (reason) {
                        case "gotFound":
                            sendCustomMessage(player, "<red>Du wurdest gefunden!</red>");
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1f, 1.5f);
                            player.teleportAsync(loc);
                            player.setAllowFlight(true);
                            gameInfo.remove(player);

                            // in whatever case the players who are not in the game will see the player again but the player who quit will see the gamers not anymore
                            for (Player outsider : Bukkit.getOnlinePlayers()) {
                                outsider.showPlayer(plugin, player);
                                player.showPlayer(plugin, outsider);
                            }
                            for (Player groupPlayer : groupPlayers) {
                                groupPlayer.hidePlayer(plugin, player);
                                player.hidePlayer(plugin, groupPlayer);
                            }


                            //removes the player from the gameGroup if he is a seeker
                            if (!groupPlayers.isEmpty()) {
                                // Removes the player safe from the List
                                groupPlayers.removeIf(value -> value.equals(player));
                            }

                            for (Player p : groupPlayers) {
                                sendCustomMessage(p, "<gold>" + player.getName() + "</gold> wurde gefunden!");
                                p.playSound(player.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1f, 1.5f);
                            }
                            break;
                        case "quit":
                            sendCustomMessage(player, "<red>Du hast das Spiel verlassen.</red>");
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1f, 1.5f);
                            player.teleportAsync(loc);
                            player.setAllowFlight(true);
                            gameInfo.remove(player);

                            // in whatever case the players who are not in the game will see the player again but the player who quit will see the gamers not anymore
                            for (Player outsider : Bukkit.getOnlinePlayers()) {
                                outsider.showPlayer(plugin, player);
                                player.showPlayer(plugin, outsider);
                            }
                            for (Player groupPlayer : groupPlayers) {
                                groupPlayer.hidePlayer(plugin, player);
                                player.hidePlayer(plugin, groupPlayer);
                            }

                            seekerRemove(player, groupName, plugin);

                            //removes the player from the gameGroup if he is a seeker
                            if (!groupPlayers.isEmpty()) {
                                // Removes the player safe from the List
                                groupPlayers.removeIf(value -> value.equals(player));
                            }


                            break;

                        case "disconnected":
                            player.teleportAsync(loc);
                            player.setAllowFlight(true);
                            gameInfo.remove(player);

                            // in whatever case the players who are not in the game will see the player again but the player who quit will see the gamers not anymore
                            for (Player outsider : Bukkit.getOnlinePlayers()) {
                                outsider.showPlayer(plugin, player);
                                player.showPlayer(plugin, outsider);
                            }
                            for (Player groupPlayer : groupPlayers) {
                                groupPlayer.hidePlayer(plugin, player);
                                player.hidePlayer(plugin, groupPlayer);
                            }

                            seekerRemove(player, groupName, plugin);

                            //removes the player from the gameGroup if he is a seeker
                            if (!groupPlayers.isEmpty()) {
                                // Removes the player safe from the List
                                groupPlayers.removeIf(value -> value.equals(player));
                            }

                            for (Player p : groupPlayers) {
                                sendCustomMessage(p, "<gold>" + player.getName() + "<gold><red> hat das Spiel verlassen.</red>");
                            }
                            break;

                        case "gameEnd":
                            // Teleport all players back to the spawn location
                            if (!groupPlayers.isEmpty()) {

                                for (Player p1 : Bukkit.getOnlinePlayers()) {
                                    for (Player p2 : Bukkit.getOnlinePlayers()) {
                                        if (!p1.equals(p2)) p1.showPlayer(plugin, p2);
                                    }
                                }

                                for (Player p : groupPlayers) {
                                    seekerRemove(p, groupName, plugin);
                                    p.teleportAsync(loc);
                                    p.setAllowFlight(true);
                                    p.setGlowing(false);
                                    gameInfo.remove(p);
                                    hiddenNameTag.removePlayer(p);
                                }
                                gameGroup.remove(groupName);
                                seekerGroup.remove(groupName);
                                gameState.remove(groupName);
                            }
                            break;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    // removes the player from the seekerGroup if he is a seeker
    public static void seekerRemove(Player player, String groupName, JavaPlugin plugin) {
        if (seekerGroup.containsKey(groupName)) {
            seekerGroup.get(groupName).remove(player);
            player.getAttribute(Attribute.SCALE).setBaseValue(1.0);
            player.getInventory().setItem(plugin.getConfig().getInt("small-modus.slot") - 1, null);
            if (!noMoveGroup.isEmpty()) {
                noMoveGroup.get(groupName).remove(player);
            }
        }
    }
}
