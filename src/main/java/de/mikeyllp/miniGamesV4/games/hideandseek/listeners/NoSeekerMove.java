package de.mikeyllp.miniGamesV4.games.hideandseek.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.Map;

import static de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups.noMoveGroup;

public class NoSeekerMove implements Listener {

    // That the player cannot move if he is a seeker
    @EventHandler
    public void onSeekerMove(PlayerMoveEvent event) {
        for (Map.Entry<String, List<Player>> entry : noMoveGroup.entrySet()) {
            List<Player> seekerList = entry.getValue();
            if (seekerList.contains(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }
}
