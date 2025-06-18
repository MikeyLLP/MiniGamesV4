package de.mikeyllp.miniGamesV4.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import static de.mikeyllp.miniGamesV4.games.hideandseek.HideAndSeekGame.addPlayerToHAS;
import static de.mikeyllp.miniGamesV4.storage.ClickInviteStorage.enableListener;
import static de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.gameInfo;
import static de.mikeyllp.miniGamesV4.utils.ClickInviteUtils.enableClickInvite;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.*;


public class MenuMain {

    //This method creates a new GUI with the title "Mini Games" and shows it to the player
    public static void openGameMenue(Player player, JavaPlugin plugin) {
        //Creates a new GUI with 3 rows and the title "Mini Games"
        ChestGui gui = new ChestGui(3, "MiniGames");
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

        FileConfiguration config = plugin.getConfig();

        // Tic Tac Toe Item
        ItemStack tttItem = createGUIItem(Material.PAPER, "TicTacToe");


        // RPS Item
        ItemStack rpsItem = createGUIItem(Material.SHEARS, "Schere Stein Papier");

        // HAS Item
        ItemStack hasItem = createGUIItem(Material.SPYGLASS, "Hide And Seek");

        //Adds  if it enabled the items to the GUI
        if (config.getBoolean("TicTacToe")) {
            navigatorPane.addItem(new GuiItem(tttItem, event -> {
                event.setCancelled(true);
                if (canPlay(player)) return;
                if (!checkPlayer(player)) return;
                enableClickInvite(player, "TicTacToe");
            }));
        }

        if (config.getBoolean("RockPaperScissors")) {
            navigatorPane.addItem(new GuiItem(rpsItem, event -> {
                event.setCancelled(true);
                if (canPlay(player)) return;
                if (!checkPlayer(player)) return;
                enableClickInvite(player, "RPS");
            }));
        }

        if (config.getBoolean("HideAndSeek")) {
            navigatorPane.addItem(new GuiItem(hasItem, event -> {
                event.setCancelled(true);
                if (!checkPlayer(player)) return;
                Player sender = (Player) event.getWhoClicked();

                if (gameInfo.containsKey(sender)) {
                    sendAlreadyInGameMessage(sender);
                    player.closeInventory();
                    return;
                }

                addPlayerToHAS(sender, plugin);
                player.closeInventory();

            }));
        }

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

    private static ItemStack createGUIItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(name));
        meta.addEnchant(Enchantment.UNBREAKING, 1, true); // Fake-Enchant
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);        // Nur Glitzern, kein Text

        item.setItemMeta(meta);
        return item;
    }

    public static boolean canPlay(Player player) {
        if (gameInfo.containsKey(player)) {
            sendAlreadyInGameMessage(player);
            player.closeInventory();
            return true;
        }
        return false;
    }
}
