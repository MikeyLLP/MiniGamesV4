package de.mikeyllp.miniGamesV4.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekStorage.listUntilX;
import static de.mikeyllp.miniGamesV4.game.hideandseek.utils.removePlayersHideAndSeek.playerRemove;
import static de.mikeyllp.miniGamesV4.game.rps.RPSGame.inGameStatus;
import static de.mikeyllp.miniGamesV4.game.rps.RPSGame.removePlayersFromList;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class PlayerQuitListener implements Listener {

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
        playerRemove(player, "disconnected");

    }
}
