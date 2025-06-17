package de.mikeyllp.miniGamesV4.games.hideandseek.listeners;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

import static de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups.*;
import static de.mikeyllp.miniGamesV4.games.hideandseek.utils.removePlayersHideAndSeek.playerRemove;

public class HideAndSeekListeners implements Listener {

    private final JavaPlugin plugin;

    public HideAndSeekListeners(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // For the Hide and Seek game to
    @EventHandler
    public void onClickEvent(PrePlayerAttackEntityEvent event) {


        if (event.getAttacked() instanceof Player) {
            Player player = event.getPlayer();
            event.setCancelled(true);
            // So that the seekers cannot direktly click on the players to remove them
            for (Map.Entry<String, List<Player>> entry : noMoveGroup.entrySet()) {
                List<Player> seekerList = entry.getValue();
                if (seekerList.contains(player)) {
                    return;
                }
            }

            // We chack all the seeker groups if the player is a seeker
            for (Map.Entry<String, List<Player>> entry : seekerGroup.entrySet()) {
                String groupName = entry.getKey();
                List<Player> seekerList = entry.getValue();
                // Check if the player is a seeker
                if (seekerList.contains(player)) {
                    List<Player> playerList = gameGroup.get(groupName);
                    // Get the player who was clicked
                    Player clickedPlayer = (Player) event.getAttacked();
                    if (playerList != null && playerList.contains(clickedPlayer)) {
                        playerRemove(clickedPlayer, "gotFound", plugin);
                    }
                }
            }
        }
    }
}
