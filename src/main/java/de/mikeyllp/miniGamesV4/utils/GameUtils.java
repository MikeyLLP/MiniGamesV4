package de.mikeyllp.miniGamesV4.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;


public class GameUtils {
    public static void checkGameResultForPlayers(Player winner, Player loser, int winnerCase, boolean playSound, boolean sendMessage) {
        //Sends a message to the players indicating if they have won, lost, or if it is a draw
        switch (winnerCase) {
            case 1:
                //send a message to the players when it's enabled
                if (sendMessage) {
                    sendCustomMessage(winner, "<COLOR:#00E5E5>Du hast Gewonnen!</COLOR>");
                    sendCustomMessage(loser, "<COLOR:#00E5E5>Du hast Verloren :(</COLOR>");
                }
                //Play a sound only if it's enabled.
                if (playSound) {
                    Location winnerPos = winner.getLocation();
                    Location loserPos = loser.getLocation();
                    winner.playSound(winnerPos, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                    loser.playSound(loserPos, Sound.ENTITY_VILLAGER_DEATH, 1.0F, 1.0F);
                }
                break;
            case 2:
                //send a message to the players when it's enabled
                if (sendMessage) {
                    sendCustomMessage(winner, "<COLOR:#00E5E5>Unentschieden!</COLOR>");
                    sendCustomMessage(loser, "<COLOR:#00E5E5>Unentschieden!</COLOR>");
                }
                //Play a sound only if it's enabled.
                if (playSound) {
                    Location player1Pos = winner.getLocation();
                    Location player2Pos = loser.getLocation();
                    winner.playSound(player1Pos, Sound.BLOCK_ANVIL_LAND, 0.3F, 1.2F);
                    loser.playSound(player2Pos, Sound.BLOCK_ANVIL_LAND, 0.3F, 1.2F);
                }
                break;
        }

    }
}
