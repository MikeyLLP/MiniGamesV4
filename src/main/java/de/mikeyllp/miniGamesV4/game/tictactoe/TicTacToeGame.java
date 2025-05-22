package de.mikeyllp.miniGamesV4.game.tictactoe;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.DispenserGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import de.mikeyllp.miniGamesV4.MiniGamesV4;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.gameInfo;
import static de.mikeyllp.miniGamesV4.utils.GameUtils.checkGameResultForPlayers;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;


public class TicTacToeGame implements Listener {

    // This is very useful; it helps to check if the game is won
    private static final Map<Player, TicTacToeGame.GameState> playerGameState = new HashMap<>();

    // This allows setting it for each player individually so that another game cannot be disrupted
    public static class GameState {
        // This boolean indicates whether the game is won or not
        boolean gameWon = false;
        // This is used to switch between X and O
        boolean isXTurn = true;
        // This counts up with every move made
        int movesMade = 0;
        // This is the virtual list of the game; it is very useful because it allows checking if a player has won
        Material[] state = new Material[9];
    }

    public static void openTicTacToe(Player inviter, Player invited) {


        // The pane displayed to the player
        OutlinePane contentsA = new OutlinePane(0, 0, 3, 3);

        // X and O Items
        ItemStack greenPane = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        GuiItem xItem = new GuiItem(greenPane);

        ItemStack redPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        GuiItem oItem = new GuiItem(redPane);


        GameState state = new GameState();

        // Both players get added to the game
        playerGameState.put(inviter.getPlayer(), state);
        playerGameState.put(invited.getPlayer(), state);

        // This is the GUI that shows to the Player
        DispenserGui guiA = new DispenserGui("Tic Tac Toe");

        // This cancels the click event so that the player cannot edit the GUI
        guiA.setOnGlobalClick(event -> event.setCancelled(true));

        // This is for the randomizer that makes who starts first
        int randomNumber = (int) (Math.random() * 2);
        String prefix = "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR>";
        switch (randomNumber) {
            case 0:
                sendCustomMessage(inviter, "<color:#00E5E5>Du beginnst<color:#00E5E5>");
                sendCustomMessage(invited, "<gold>" + inviter.getName() + "</gold><color:#00E5E5> beginnt!<color:#00E5E5>");
                state.isXTurn = !state.isXTurn;
                break;
            case 1:
                sendCustomMessage(invited, "<color:#00E5E5>Du beginnst<color:#00E5E5>");
                sendCustomMessage(inviter, "<gold>" + invited.getName() + "</gold><color:#00E5E5> beginnt!<color:#00E5E5>");
                break;
        }


        // This fills all the slots with the black glass pane
        for (int i = 0; i < 9; i++) {
            final int slot = i;
            // The pane contains a click event that checks if the player is allowed to play and adds the item to the slot.
            GuiItem cell = new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), event -> {

                // All events are canceled if the game is won
                if (state.gameWon) return;
                if (state.state[slot] != null) return;
                // This checks which player is allowed to play
                if (state.isXTurn) {

                    if (event.getWhoClicked().equals(inviter)) {
                        sendCustomMessage(inviter, "<red>Du bist nicht dran!</red>");
                    } else {
                        contentsA.getItems().set(slot, oItem);
                        // When the player clicks on the slot it gets set to the red glass pane
                        state.state[slot] = Material.RED_STAINED_GLASS_PANE;

                        // The moves counts up
                        state.movesMade++;
                        // Switches the turn
                        state.isXTurn = !state.isXTurn;
                        guiA.update();
                    }
                } else {
                    if (event.getWhoClicked().equals(invited)) {
                        sendCustomMessage(invited, "<red>Du bist nicht dran!</red>");
                    } else {
                        contentsA.getItems().set(slot, xItem);
                        state.state[slot] = Material.GREEN_STAINED_GLASS_PANE;

                        state.movesMade++;
                        state.isXTurn = !state.isXTurn;
                        guiA.update();
                    }
                }

                // Checks if the game is won or not
                checkWin(state, Material.GREEN_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE, inviter, invited, guiA, contentsA);
            });
            // Adds the item to the pane
            contentsA.addItem(cell);
        }
        guiA.getContentsComponent().addPane(contentsA);

        // Shows the GUI to both players
        guiA.show(inviter);
        guiA.show(invited);
    }

    // Checks if the player closes the GUI by himself
    @EventHandler
    public static void onPlayerCloseInventoryEvent(InventoryCloseEvent event) {


        InventoryCloseEvent.Reason reason = event.getReason();

        // This check is very important it Checks if the player closes the GUI by himself or the plugin reloaded it
        if (reason == InventoryCloseEvent.Reason.PLAYER) {
            Player eventPlayer = (Player) event.getPlayer();
            if (gameInfo.containsKey(eventPlayer)) {
                GameState state = playerGameState.get(eventPlayer);

                if (state == null || state.gameWon) return;

                // Note: maybe I play a sound if one gives up
                Player player = gameInfo.get(eventPlayer);
                sendCustomMessage(player, "<gold>" + eventPlayer.getName() + "</gold> hat aufgegeben.</color:#00E5E5>");
                sendCustomMessage(eventPlayer, "Du hast aufgegeben.</color:#00E5E5>");

                // Deletes all the players from the game
                gameInfo.remove(eventPlayer);
                gameInfo.remove(player);

                playerGameState.remove(eventPlayer);
                playerGameState.remove(player);
                // Closes the inventory of the both player
                player.closeInventory();
                eventPlayer.closeInventory();

            }
        }
    }

    private static void checkWin(GameState state, Material xPane, Material oPane, Player player1, Player player2, DispenserGui gui, OutlinePane contents) {
        // Here are all the winning combinations
        int[][] winningCombos = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };

        boolean hasWinner = false;
        int winnerCase = 0;

        // Checks all Combos if anyone has won
        for (int[] combo : winningCombos) {
            // This is the first player
            if (state.state[combo[0]] == xPane &&
                    state.state[combo[1]] == xPane &&
                    state.state[combo[2]] == xPane) {
                markWinningLine(contents, gui, combo, player1, player2);
                hasWinner = true;
                winnerCase = 1;
            }
            // This is the second player
            if (state.state[combo[0]] == oPane &&
                    state.state[combo[1]] == oPane &&
                    state.state[combo[2]] == oPane) {
                markWinningLine(contents, gui, combo, player2, player1);
                // setzt von false auf true
                hasWinner = true;
                winnerCase = 2;
            }
            // If the game is a draw, 9 moves have been made, and no one has won
            if (!hasWinner && state.movesMade == 9) {
                hasWinner = true;
                winnerCase = 3;
            }
        }
        if (hasWinner) {
            state.gameWon = true;
            final int finalWinnerCase = winnerCase;
            // This adds a delay so that the players can see the winning line
            new BukkitRunnable() {
                @Override
                public void run() {
                    player1.closeInventory();
                    player2.closeInventory();
                    // Sends a message to the players indicating if they have won, lost, or if it is a draw
                    switch (finalWinnerCase) {
                        case 1:
                            checkGameResultForPlayers(player1, player2, 1, false, true);
                            gameInfo.remove(player1);
                            gameInfo.remove(player2);
                            break;
                        case 2:
                            checkGameResultForPlayers(player2, player1, 1, false, true);
                            gameInfo.remove(player1);
                            gameInfo.remove(player2);
                            break;
                        case 3:
                            checkGameResultForPlayers(player1, player2, 2, true, true);
                            gameInfo.remove(player1);
                            gameInfo.remove(player2);
                            break;
                    }
                }
            }.runTaskLater(MiniGamesV4.getInstance(), 20L);
        }
    }

    // Marks the winning line and plays a sound for the players
    private static void markWinningLine(OutlinePane contents, DispenserGui gui, int[] slots, Player winner, Player loser) {
        checkGameResultForPlayers(winner, loser, 1, true, false);
        ItemStack purplePane = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        GuiItem highlight = new GuiItem(purplePane);
        // Highlights the winning line with purple glass panes
        for (int slot : slots) {
            contents.getItems().set(slot, highlight);
        }
        gui.update();
    }
}