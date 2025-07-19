package de.mikeyllp.miniGamesV4.utils

import org.bukkit.Sound
import org.bukkit.entity.Player

object GameUtils {
    fun checkGameResultForPlayers(
        winner: Player?,
        loser: Player,
        winnerCase: Int,
        playSound: Boolean,
        sendMessage: Boolean
    ) {
        //Sends a message to the players indicating if they have won, lost, or if it is a draw
        when (winnerCase) {
            1 -> {
                //send a message to the players when it's enabled
                if (sendMessage) {
                    MessageUtils.sendMessage(winner, "<COLOR:#00E5E5>Du hast Gewonnen!</COLOR>")
                    MessageUtils.sendMessage(loser, "<COLOR:#00E5E5>Du hast Verloren :(</COLOR>")
                }
                //Play a sound only if it's enabled.
                if (playSound) {
                    val winnerPos = winner.location
                    val loserPos = loser.location
                    winner.playSound(winnerPos, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
                    loser.playSound(loserPos, Sound.ENTITY_VILLAGER_DEATH, 1.0f, 1.0f)
                }
            }

            2 -> {
                //send a message to the players when it's enabled
                if (sendMessage) {
                    MessageUtils.sendMessage(winner, "<COLOR:#00E5E5>Unentschieden!</COLOR>")
                    MessageUtils.sendMessage(loser, "<COLOR:#00E5E5>Unentschieden!</COLOR>")
                }
                //Play a sound only if it's enabled.
                if (playSound) {
                    val player1Pos = winner.location
                    val player2Pos = loser.location
                    winner.playSound(player1Pos, Sound.BLOCK_ANVIL_LAND, 0.3f, 1.2f)
                    loser.playSound(player2Pos, Sound.BLOCK_ANVIL_LAND, 0.3f, 1.2f)
                }
            }
        }
    }
}
