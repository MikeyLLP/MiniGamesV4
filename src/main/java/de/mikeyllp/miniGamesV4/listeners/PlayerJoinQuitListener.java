package de.mikeyllp.miniGamesV4.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekGameGroups.*;
import static de.mikeyllp.miniGamesV4.game.hideandseek.utils.removePlayersHideAndSeek.playerRemove;
import static de.mikeyllp.miniGamesV4.game.rps.RPSGame.inGameStatus;
import static de.mikeyllp.miniGamesV4.game.rps.RPSGame.removePlayersFromList;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class PlayerJoinQuitListener implements Listener {

    private final JavaPlugin plugin;

    public PlayerJoinQuitListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    //removes the player from the game when he leaves the server
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Check if the player was in RPS game
        if (inGameStatus.containsKey(player)) {
            sendCustomMessage(inGameStatus.get(player), "<red>" + player.getName() + " hat das Spiel verlassen!</red>");
            removePlayersFromList(player, inGameStatus.get(player));
        }

        // Removes The Player from the listUntilX
        listUntilX.removeIf(value -> value.equals(player));
        playerRemove(player, "disconnected", plugin);

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!gameState.isEmpty()) {
            for (Map.Entry<String, List<Player>> entry : gameGroup.entrySet()) {
                List<Player> groupPlayers = entry.getValue();
                for (Player groupPlayer : groupPlayers) {
                    groupPlayer.hidePlayer(plugin, player);
                }
                for (Player outsider : Bukkit.getOnlinePlayers()) {
                    if (groupPlayers.contains(outsider)) continue;
                    for (Player hidden : groupPlayers) {
                        outsider.hidePlayer(plugin, hidden);
                    }
                }
            }
        }
    }
}
