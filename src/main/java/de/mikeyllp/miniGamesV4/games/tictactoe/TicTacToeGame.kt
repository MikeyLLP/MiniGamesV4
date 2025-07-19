package de.mikeyllp.miniGamesV4.games.tictactoe

import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.DispenserGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import de.mikeyllp.miniGamesV4.MiniGamesV4
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage
import de.mikeyllp.miniGamesV4.utils.GameUtils
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Consumer

object TicTacToeGame : Listener {
    // This is very useful; it helps to check if the game is won
    private val playerGameState: MutableMap<Player, GameState> = HashMap<Player, GameState>()

    fun openTicTacToe(inviter: Player, invited: Player) {
        // The pane displayed to the player


        val contentsA = OutlinePane(0, 0, 3, 3)

        // X and O Items
        val greenPane = ItemStack(Material.GREEN_STAINED_GLASS_PANE)
        val xItem = GuiItem(greenPane)

        val redPane = ItemStack(Material.RED_STAINED_GLASS_PANE)
        val oItem = GuiItem(redPane)


        val state = GameState()

        // Both players get added to the game
        playerGameState.put(inviter, state)
        playerGameState.put(invited, state)

        // This is the GUI that shows to the Player
        val guiA = DispenserGui("Tic Tac Toe")

        // This makes that the  Items in the GUI cant be moved
        guiA.setOnGlobalClick(Consumer { event: InventoryClickEvent -> event.isCancelled })
        guiA.setOnBottomClick(Consumer { event: InventoryClickEvent -> event.isCancelled })
        guiA.setOnGlobalDrag(Consumer { event: InventoryDragEvent -> event.isCancelled })

        // This is for the randomizer that makes who starts first
        val randomNumber = (Math.random() * 2).toInt()
        when (randomNumber) {
            0 -> {
                MessageUtils.sendMessage(inviter, "<color:#00E5E5>Du beginnst<color:#00E5E5>")
                MessageUtils.sendMessage(
                    invited,
                    "<gold>$inviter.name</gold><color:#00E5E5> beginnt!<color:#00E5E5>"
                )
                state.isXTurn = !state.isXTurn
            }

            1 -> {
                MessageUtils.sendMessage(invited, "<color:#00E5E5>Du beginnst<color:#00E5E5>")
                MessageUtils.sendMessage(
                    inviter,
                    "<gold>$invited.name</gold><color:#00E5E5> beginnt!<color:#00E5E5>"
                )
            }
        }


        // This fills all the slots with the black glass pane
        for (i in 0..8) {
            val slot = i
            // The pane contains a click event that checks if the player is allowed to play and adds the item to the slot.
            val cell = GuiItem(ItemStack(Material.BLACK_STAINED_GLASS_PANE), Consumer { event: InventoryClickEvent? ->

                // All events are canceled if the game is won
                if (state.gameWon) return@Consumer
                if (state.state[slot] != null) return@Consumer
                // This checks which player is allowed to play
                if (state.isXTurn) {
                    if (event?.whoClicked == inviter) {
                        MessageUtils.sendMessage(inviter, "<red>Du bist nicht dran!</red>")
                    } else {
                        contentsA.items[slot] = oItem
                        // When the player clicks on the slot it gets set to the red glass pane
                        state.state[slot] = Material.RED_STAINED_GLASS_PANE

                        // The moves counts up
                        state.movesMade++
                        // Switches the turn
                        state.isXTurn = !state.isXTurn
                        guiA.update()
                    }
                } else {
                    if (event!!.whoClicked == invited) {
                        MessageUtils.sendMessage(invited, "<red>Du bist nicht dran!</red>")
                    } else {
                        contentsA.items[slot] = xItem
                        state.state[slot] = Material.GREEN_STAINED_GLASS_PANE

                        state.movesMade++
                        state.isXTurn = !state.isXTurn
                        guiA.update()
                    }
                }

                // Checks if the game is won or not
                checkWin(
                    state,
                    Material.GREEN_STAINED_GLASS_PANE,
                    Material.RED_STAINED_GLASS_PANE,
                    inviter,
                    invited,
                    guiA,
                    contentsA
                )
            })
            // Adds the item to the pane
            contentsA.addItem(cell)
        }
        guiA.contentsComponent.addPane(contentsA)

        // Shows the GUI to both players
        guiA.show(inviter)
        guiA.show(invited)
    }

    // Checks if the player closes the GUI by himself
    @EventHandler
    fun onPlayerCloseInventoryEvent(event: InventoryCloseEvent) {
        val reason = event.reason

        // This check is very important it Checks if the player closes the GUI by himself or the plugin reloaded it
        if (reason == InventoryCloseEvent.Reason.PLAYER) {
            val eventPlayer = event.player as Player
            if (InvitePlayerStorage.gameInfo.containsKey(eventPlayer)) {
                val state = playerGameState[eventPlayer]

                if (state == null || state.gameWon) return

                // Note: maybe I play a sound if one gives up
                val player: Player = InvitePlayerStorage.gameInfo[eventPlayer]!!
                MessageUtils.sendMessage(
                    player,
                    "<gold>" + eventPlayer.name + "</gold> hat aufgegeben.</color:#00E5E5>"
                )
                MessageUtils.sendMessage(eventPlayer, "Du hast aufgegeben.</color:#00E5E5>")

                // Deletes all the players from the game
                InvitePlayerStorage.gameInfo.remove(eventPlayer)
                InvitePlayerStorage.gameInfo.remove(player)

                playerGameState.remove(eventPlayer)
                playerGameState.remove(player)
                // Closes the inventory of the both player
                player.closeInventory()
                eventPlayer.closeInventory()
            }
        }
    }

    private fun checkWin(
        state: GameState,
        xPane: Material,
        oPane: Material,
        player1: Player,
        player2: Player,
        gui: DispenserGui,
        contents: OutlinePane
    ) {
        // Here are all the winning combinations
        val winningCombos = arrayOf<IntArray>(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
        )

        var hasWinner = false
        var winnerCase = 0

        // Checks all Combos if anyone has won
        for (combo in winningCombos) {
            // This is the first player
            if (state.state[combo[0]] == xPane && state.state[combo[1]] == xPane && state.state[combo[2]] == xPane) {
                markWinningLine(contents, gui, combo, player1, player2)
                hasWinner = true
                winnerCase = 1
            }
            // This is the second player
            if (state.state[combo[0]] == oPane && state.state[combo[1]] == oPane && state.state[combo[2]] == oPane) {
                markWinningLine(contents, gui, combo, player2, player1)
                // setzt von false auf true
                hasWinner = true
                winnerCase = 2
            }
            // If the game is a draw, 9 moves have been made, and no one has won
            if (!hasWinner && state.movesMade == 9) {
                hasWinner = true
                winnerCase = 3
            }
        }
        if (hasWinner) {
            state.gameWon = true
            val finalWinnerCase = winnerCase
            // This adds a delay so that the players can see the winning line
            object : BukkitRunnable() {
                override fun run() {
                    player1.closeInventory()
                    player2.closeInventory()
                    // Sends a message to the players indicating if they have won, lost, or if it is a draw
                    when (finalWinnerCase) {
                        1 -> {
                            GameUtils.checkGameResultForPlayers(player1, player2, 1, false, true)
                            InvitePlayerStorage.gameInfo.remove(player1)
                            InvitePlayerStorage.gameInfo.remove(player2)
                        }

                        2 -> {
                            GameUtils.checkGameResultForPlayers(player2, player1, 1, false, true)
                            InvitePlayerStorage.gameInfo.remove(player1)
                            InvitePlayerStorage.gameInfo.remove(player2)
                        }

                        3 -> {
                            GameUtils.checkGameResultForPlayers(player1, player2, 2, true, true)
                            InvitePlayerStorage.gameInfo.remove(player1)
                            InvitePlayerStorage.gameInfo.remove(player2)
                        }
                    }
                }
            }.runTaskLater(MiniGamesV4.Companion.instance!!, 20L)
        }
    }

    // Marks the winning line and plays a sound for the players
    private fun markWinningLine(
        contents: OutlinePane,
        gui: DispenserGui,
        slots: IntArray,
        winner: Player,
        loser: Player
    ) {
        GameUtils.checkGameResultForPlayers(winner, loser, 1, true, false)
        val purplePane = ItemStack(Material.PURPLE_STAINED_GLASS_PANE)
        val highlight = GuiItem(purplePane)
        // Highlights the winning line with purple glass panes
        for (slot in slots) {
            contents.getItems().set(slot, highlight)
        }
        gui.update()
    }

    // This allows setting it for each player individually so that another game cannot be disrupted
    class GameState {
        // This boolean indicates whether the game is won or not
        var gameWon: Boolean = false

        // This is used to switch between X and O
        var isXTurn: Boolean = true

        // This counts up with every move made
        var movesMade: Int = 0

        // This is the virtual list of the game; it is very useful because it allows checking if a player has won
        var state: Array<Material?> = arrayOfNulls<Material>(9)
    }
}