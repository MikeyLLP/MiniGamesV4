package de.mikeyllp.miniGamesV4.game.hideandseek;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekStorage.gameGroup;
import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekStorage.seekerGroup;
import static de.mikeyllp.miniGamesV4.game.hideandseek.utils.removePlayersHideAndSeek.playerRemove;

public class HideAndSeekListeners implements Listener {


    // For the Hide and Seek game to
    @EventHandler
    public void onClickEvent(PrePlayerAttackEntityEvent event) {

        if (event.getAttacked() instanceof Player) {
            Player player = event.getPlayer();
            // Check if the player is a seeker
            if (seekerGroup.containsKey(player)) {
                List<Player> playerList = gameGroup.get(seekerGroup.get(player));
                // Get the player who was clicked
                Player clickedPlayer = (Player) event.getAttacked();
                for (Player p : playerList) {
                    if (clickedPlayer.equals(p)) {
                        playerRemove(clickedPlayer, "gotFound");
                    }
                }
            }
        }
    }

}
