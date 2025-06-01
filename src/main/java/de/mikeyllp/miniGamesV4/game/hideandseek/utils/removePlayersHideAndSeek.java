package de.mikeyllp.miniGamesV4.game.hideandseek.utils;


import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekGameGroups.*;
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
                    switch (reason) {
                        case "gotFound":
                            sendCustomMessage(player, "<red>Du wurdest gefunden!</red>");
                            player.teleportAsync(loc);
                            gameInfo.remove(player);
                            player.setAllowFlight(true);


                            //removes the player from the gameGroup if he is a seeker
                            if (!groupPlayers.isEmpty()) {
                                // Removes the player safe from the List
                                groupPlayers.removeIf(value -> value.equals(player));
                            }

                            for (Player p : groupPlayers) {
                                sendCustomMessage(p, "<gold>" + player.getName() + "</gold> wurde gefunden!");
                            }
                            break;

                        case "quit":
                            sendCustomMessage(player, "<red>Du hast das Spiel verlassen.</red>");
                            player.teleportAsync(loc);
                            gameInfo.remove(player);
                            player.setAllowFlight(true);

                            // removes the player from the seekerGroup if he is a seeker
                            if (seekerGroup.containsKey(groupName) && !seekerGroup.get(groupName).isEmpty()) {
                                seekerGroup.get(groupName).remove(player);
                                noMoveGroup.get(groupName).remove(player);
                            }

                            //removes the player from the gameGroup if he is a seeker
                            if (!groupPlayers.isEmpty()) {
                                // Removes the player safe from the List
                                groupPlayers.removeIf(value -> value.equals(player));
                            }

                            break;

                        case "disconnected":
                            player.teleportAsync(loc);
                            gameInfo.remove(player);
                            player.setAllowFlight(true);

                            // removes the player from the seekerGroup if he is a seeker
                            if (seekerGroup.containsKey(groupName)) {
                                seekerGroup.get(groupName).remove(player);
                                noMoveGroup.get(groupName).remove(player);
                            }

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
                            if (groupPlayers != null) {
                                for (Player p : groupPlayers) {
                                    p.teleportAsync(loc);
                                    gameInfo.remove(p);
                                    p.setAllowFlight(true);
                                }
                                gameGroup.remove(groupName);
                                seekerGroup.remove(groupName);
                            }
                            break;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
