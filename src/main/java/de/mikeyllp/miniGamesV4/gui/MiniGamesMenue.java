package de.mikeyllp.miniGamesV4.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;

import static de.mikeyllp.miniGamesV4.map.ClickInviteHashMap.addEnableListener;
import static de.mikeyllp.miniGamesV4.map.ClickInviteHashMap.enableListener;


public class MiniGamesMenue {

    //This method creates a new GUI with the title "Mini Games" and shows it to the player
    public static void openGameMenue(Player player){
        //Creates a new GUI with 3 rows and the title "Mini Games"
        ChestGui gui = new ChestGui(3, "Mini Games");
        //This makes taht the  Items in the GUI cant be moved
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        //Says that all background items can be replaced caus it has the Lowest Priority
        OutlinePane background = new OutlinePane(0, 0, 9,3, Pane.Priority.LOWEST);
        //this is the item that will be used as background
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        //fills all the empty spaces with the background item
        background.setRepeat(true);
        gui.addPane(background);

        OutlinePane navigatorPane = new OutlinePane(3, 1, 3,1);

        // Tic Tac Toe Item
        ItemStack tttItem = new ItemStack(Material.PAPER);
        ItemMeta tttMeta = tttItem.getItemMeta();
        tttMeta.displayName(Component.text("Tic Tac Toe"));
        tttItem.setItemMeta(tttMeta);

        navigatorPane.addItem(new GuiItem(tttItem, event -> {
            String prefix = "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR>";
            if (enableListener.containsKey(player)){
                player.sendRichMessage(prefix + "<red>Du musst erst ein Spieler aussuchen!</red>");
            } else {
                MiniMessage mm = MiniMessage.miniMessage();
                Component miniGameComponent = mm.deserialize("<gradient:#00FF00:#007F00>MiniGames</gradient>");
                player.closeInventory();
                player.showTitle(Title.title(miniGameComponent,
                        Component.text("§6Klicke einen Spieler an, um mit ihm zu spielen"), Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))));
                addEnableListener(player, "TicTacToe");
            }
        }));

        gui.addPane(navigatorPane);

        //Shows the GUI to the player
        gui.show(player);
    }
}
