package de.mikeyllp.miniGamesV4.games.rps

import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage
import de.mikeyllp.miniGamesV4.utils.GameUtils
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.time.Duration
import java.util.*

class RPSGame : Listener {
    class GameState {
        var inviter: Player? = null
        var invited: Player? = null

        //to check if both players made their move
        var waitingForPlayer: Int = 0

        //points for both players
        var pointsInviter: Int = 0
        var pointsInvited: Int = 0

        //String for the fight
        var fightItemInviter: String = ""
        var fightItemInvited: String = ""
    }

    //This method is used to check which message the player sends in the chat to play the game
    @EventHandler
    fun onPlayerChat(event: AsyncChatEvent) {
        //game State

        val state: GameState? = playerGameState.get(event.getPlayer())

        if (state == null) return

        //get the message from the event and convert it to lower case so that the player can write it in any case
        val msg = PlainTextComponentSerializer.plainText().serialize(event.message()).trim { it <= ' ' }
            .lowercase(Locale.getDefault())


        val mm = MiniMessage.miniMessage()
        val failMsg = mm.deserialize(
            MessageUtils.prefix() + "<red>Bitte wähle <bold><gray><insert:Schere><hover:show_text:'Shift + Klick zum Auswählen'>[Schere]</hover></insert></gray></bold>," +
                    " <bold><dark_gray><insert:Stein><hover:show_text:'Shift + Klick zum Auswählen'>[Stein]</hover></insert></dark_gray></bold>," +
                    " <white><bold><insert:Papier><hover:show_text:'Shift + Klick zum Auswählen'>[Papier]</hover></insert></bold></white>" +
                    " oder <bold><click:run_command:'/minigames quit'><hover:show_text:'Klicken zum Auswählen'>gib auf</hover></click></bold>.</red>"
        )


        if (inGameStatus.containsKey(event.getPlayer())) {
            event.setCancelled(true)
            val inviter = state.inviter!!
            val invited = state.invited!!

            //checks if the player made already his move
            if (!(msg == "cancel")) {
                if (!(state.fightItemInviter.isEmpty())) {
                    MessageUtils.sendMessage(inviter, "<red>Du hast bereits deine Wahl getroffen!</red>")
                    return
                }
            }
            //checks which message send the player
            when (msg) {
                "schere" -> {
                    state.fightItemInviter = "Schere"
                    state.waitingForPlayer++
                }

                "stein" -> {
                    state.fightItemInviter = "Stein"
                    state.waitingForPlayer++
                }

                "papier" -> {
                    state.fightItemInviter = "Papier"
                    state.waitingForPlayer++
                }

                else -> {
                    inviter.sendMessage(failMsg)
                    return
                }
            }
            //checks if both players have made their move
            if (state.waitingForPlayer == 2) {
                checkWin(inviter, invited, state)
                return
            }
            MessageUtils.sendMessage(inviter, "Warten auf anderen Spieler...</color>")
        }

        if (inGameStatus.containsValue(event.getPlayer())) {
            val inviter = state.inviter!!
            val invited = state.invited!!
            event.setCancelled(true)

            //checks if the player made already his move
            if (!(msg == "cancel")) {
                if (!(state.fightItemInvited.isEmpty())) {
                    MessageUtils.sendMessage(invited, "<red>Du hast bereits deine Wahl getroffen!</red>")
                    return
                }
            }

            //checks which message send the player
            when (msg) {
                "schere" -> {
                    state.fightItemInvited = "Schere"
                    state.waitingForPlayer++
                }

                "stein" -> {
                    state.fightItemInvited = "Stein"
                    state.waitingForPlayer++
                }

                "papier" -> {
                    state.fightItemInvited = "Papier"
                    state.waitingForPlayer++
                }

                else -> {
                    invited.sendMessage(failMsg)
                    return
                }
            }

            //checks if both players have made their move
            if (state.waitingForPlayer == 2) {
                checkWin(inviter, invited, state)
                return
            }
            MessageUtils.sendMessage(invited, "Warten auf anderen Spieler...</color>")
        }
    }

    companion object {
        //This map is used to check if a player is playing this game, in order to activate the ChatListener.
        val inGameStatus: MutableMap<Player?, Player?> = HashMap<Player?, Player?>()
        val playerGameState: MutableMap<Player?, GameState?> = HashMap<Player?, GameState?>()


        fun startRPSGame(inviter: Player, invited: Player) {
            //game State

            val state = GameState()

            //both players get added to the game
            playerGameState.put(inviter, state)
            playerGameState.put(invited, state)

            inGameStatus.put(inviter, invited)

            state.inviter = inviter
            state.invited = invited


            val prefix =
                "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR>"

            val mm = MiniMessage.miniMessage()

            val rspGameComponent = mm.deserialize("<color:#00E5E5>Erhalte 3 Punkte. Wähle im Chat aus!</color>")
            val choseMsg = mm.deserialize(
                prefix + "<bold><gray><insert:Schere><hover:show_text:'Shift + Klick zum Auswählen'>[Schere]</hover></insert>" +
                        " <dark_gray><insert:Stein><hover:show_text:'Shift + Klick zum Auswählen'>[Stein]</hover></insert>" +
                        " <white><insert:Papier><hover:show_text:'Shift + Klick zum Auswählen'>[Papier]</hover></insert></bold>" +
                        " <red><bold><click:run_command:'/minigames quit'><hover:show_text:'Klicken zum Auswählen'>gib auf</hover></click></bold></red>"
            )

            inviter.showTitle(
                Title.title(
                    Component.text("§6Spiel Startet!"), rspGameComponent, Title.Times.times(
                        Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)
                    )
                )
            )
            invited.showTitle(
                Title.title(
                    Component.text("§6Spiel Startet!"), rspGameComponent, Title.Times.times(
                        Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1)
                    )
                )
            )

            inviter.sendMessage(choseMsg)
            invited.sendMessage(choseMsg)
        }

        private fun checkWin(inviter: Player, invited: Player, state: GameState) {
            val mm = MiniMessage.miniMessage()

            val fight = mm.deserialize("<gold>" + state.fightItemInviter + " vs " + state.fightItemInvited + "</gold>")
            val draw = mm.deserialize("<color:#00E5E5>Unentschieden!</color>")


            //case if it´s a draw
            if (state.fightItemInviter == state.fightItemInvited) {
                inviter.showTitle(
                    Title.title(
                        fight,
                        draw,
                        Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
                    )
                )
                invited.showTitle(
                    Title.title(
                        fight,
                        draw,
                        Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
                    )
                )

                GameUtils.checkGameResultForPlayers(inviter, invited, 2, true, false)

                //reset the states
                state.fightItemInviter = ""
                state.fightItemInvited = ""
                state.waitingForPlayer = 0
                return
            }

            //checks if the inviter wins that player who wins get A point up
            if (state.fightItemInviter == "Stein" && state.fightItemInvited == "Schere" || state.fightItemInviter == "Schere" && state.fightItemInvited == "Papier" || state.fightItemInviter == "Papier" && state.fightItemInvited == "Stein") {
                state.pointsInviter++

                val points: String = updatePoints(state.pointsInviter, state.pointsInvited)
                val pointsB: String = updatePoints(state.pointsInvited, state.pointsInviter)

                val win = mm.deserialize("<color:#00E5E5> Du hast gewonnen! <gold>+1 Punkt</gold></color> " + points)
                val loseB = mm.deserialize("<color:#00E5E5> Du hast verloren!</color> " + pointsB)


                inviter.showTitle(
                    Title.title(
                        fight,
                        win,
                        Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
                    )
                )
                invited.showTitle(
                    Title.title(
                        fight,
                        loseB,
                        Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
                    )
                )


                GameUtils.checkGameResultForPlayers(inviter, invited, 1, true, false)
            } else {
                state.pointsInvited++

                val points: String = updatePoints(state.pointsInviter, state.pointsInvited)
                val pointsB: String = updatePoints(state.pointsInvited, state.pointsInviter)

                val winB = mm.deserialize("<color:#00E5E5> Du hast gewonnen! <gold>+1 Punkt</gold></color> " + pointsB)
                val lose = mm.deserialize("<color:#00E5E5> Du hast verloren!</color> " + points)

                invited.showTitle(
                    Title.title(
                        fight,
                        winB,
                        Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
                    )
                )
                inviter.showTitle(
                    Title.title(
                        fight,
                        lose,
                        Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
                    )
                )
                GameUtils.checkGameResultForPlayers(invited, inviter, 1, true, false)
            }
            //reset the states
            state.fightItemInviter = ""
            state.fightItemInvited = ""
            state.waitingForPlayer = 0

            //checks if one of the players has 3 points
            if (state.pointsInviter == 3) {
                GameUtils.checkGameResultForPlayers(inviter, invited, 1, false, true)
                removePlayersFromList(inviter, invited)
            } else if (state.pointsInvited == 3) {
                GameUtils.checkGameResultForPlayers(invited, inviter, 1, false, true)
                removePlayersFromList(inviter, invited)
            }
        }

        //updates the points of the players
        private fun updatePoints(you: Int, opponent: Int): String {
            return "<color:#00E5E5>Du: <gold>" + you + "</gold> | <gold>Gegner: " + opponent + "</color>"
        }

        //removes the players form the list
        fun removePlayersFromList(inviter: Player?, invited: Player?) {
            InvitePlayerStorage.gameInfo.remove(inviter)
            InvitePlayerStorage.gameInfo.remove(invited)

            playerGameState.remove(inviter)
            playerGameState.remove(invited)

            inGameStatus.remove(inviter, invited)
        }
    }
}
