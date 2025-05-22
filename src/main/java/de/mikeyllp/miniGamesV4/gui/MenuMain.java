package de.mikeyllp.miniGamesV4.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static de.mikeyllp.miniGamesV4.storage.ClickInviteStorage.enableListener;
import static de.mikeyllp.miniGamesV4.utils.ClickInviteUtils.enableClickInvite;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;


public class MenuMain {

    //This method creates a new GUI with the title "Mini Games" and shows it to the player
    public static void openGameMenue(Player player) {
        //Creates a new GUI with 3 rows and the title "Mini Games"
        ChestGui gui = new ChestGui(3, "Mini Games");
        //This makes taht the  Items in the GUI cant be moved
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        //Says that all background items can be replaced caus it has the Lowest Priority
        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        //this is the item that will be used as background
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        //fills all the empty spaces with the background item
        background.setRepeat(true);
        gui.addPane(background);

        OutlinePane navigatorPane = new OutlinePane(3, 1, 3, 1);

        // Tic Tac Toe Item
        ItemStack tttItem = new ItemStack(Material.PAPER);
        ItemMeta tttMeta = tttItem.getItemMeta();
        tttMeta.displayName(Component.text("Tic Tac Toe"));
        tttItem.setItemMeta(tttMeta);

        //RPS Item
        ItemStack rpsItem = new ItemStack(Material.SHEARS);
        ItemMeta rpsMeta = rpsItem.getItemMeta();
        rpsMeta.displayName(Component.text("Schere Stein Papier"));
        rpsItem.setItemMeta(rpsMeta);

        //Adds the items to the GUI
        navigatorPane.addItem(new GuiItem(tttItem, event -> {
            if (!checkPlayer(player)) return;
            enableClickInvite(player, "TicTacToe");

        }));

        navigatorPane.addItem(new GuiItem(rpsItem, event -> {
            if (!checkPlayer(player)) return;
            enableClickInvite(player, "RPS");
        }));

        gui.addPane(navigatorPane);

        // Shows the GUI to the player
        gui.show(player);
    }

    // This method checks if the player is in the enableListener map and sends a message if he is
    private static boolean checkPlayer(Player player) {
        if (enableListener.containsKey(player)) {
            sendCustomMessage(player, "<red>Du musst erst ein Spieler aussuchen!</red>");
            return false;
        }
        return true;
    }
}
