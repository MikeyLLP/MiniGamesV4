package de.mikeyllp.miniGamesV4.game.hideandseek.utils;

import org.bukkit.entity.Player;

import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekStorage.gameGroup;
import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekStorage.seekerGroup;
import static de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.gameInfo;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class removePlayersHideAndSeek {

    // Method to remove a player from the game group when he quit
    public static boolean playerRemove(Player player, String reason) {
        if (!gameGroup.isEmpty()) {
            for (String s : gameGroup.keySet()) {
                if (gameGroup.get(s).contains(player)) {
                    gameGroup.get(s).removeIf(value -> value.equals(player));
                    switch (reason) {
                        case "gotFound":
                            sendCustomMessage(player, "<red>Du wurdest gefunden!</red>");
                            for (Player gamePlayers : gameGroup.get(s)) {
                                gameInfo.remove(player);
                                sendCustomMessage(gamePlayers, "<gold>" + player.getName() + "</gold> wurde gefunden!");
                            }
                            break;
                        case "quit":
                            sendCustomMessage(player, "<red>Du hast das Spiel verlassen.</red>");
                            for (Player p : gameGroup.get(s)) {
                                seekerGroup.remove(player);
                                gameInfo.remove(player);
                                sendCustomMessage(p, "<gold>" + player.getName() + "<gold><red> hat das Aufgegeben.</red>");
                            }
                            break;
                        case "disconnected":
                            for (Player p : gameGroup.get(s)) {
                                seekerGroup.remove(player);
                                gameInfo.remove(player);
                                sendCustomMessage(p, "<gold>" + player.getName() + "<gold><red> hat das Spiel verlassen.</red>");
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
