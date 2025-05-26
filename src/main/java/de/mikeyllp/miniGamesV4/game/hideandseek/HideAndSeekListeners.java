package de.mikeyllp.miniGamesV4.game.hideandseek;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekStorage.gameGroup;
import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekStorage.seekerGroup;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class HideAndSeekListeners implements Listener {

    @EventHandler
    public void onClickEvent(PrePlayerAttackEntityEvent event) {

        if (event.getAttacked() instanceof Player) {
            Player player = event.getPlayer();

            // Check if the player is a seeker
            if (seekerGroup.containsKey(player)) {
                List<Player> playerList = gameGroup.get(seekerGroup.get(player));
                // Get the player who was clicked
                for (Player p : playerList) {
                    Player clickedPlayer = (Player) event.getAttacked();
                    if (clickedPlayer.equals(p)) {
                        playerList.remove(clickedPlayer);
                        sendCustomMessage(clickedPlayer, "<red>Du wurdest gefunden!</red>");
                        for (Player gamePlayers : playerList) {
                            sendCustomMessage(gamePlayers, "<gold>" + clickedPlayer.getName() + "</gold> wurde gefunden!");
                        }
                        return;
                    }
                }
            }
        }
    }

}
