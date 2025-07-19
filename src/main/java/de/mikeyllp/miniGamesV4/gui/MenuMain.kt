package de.mikeyllp.miniGamesV4.gui

import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.Pane
import de.mikeyllp.miniGamesV4.games.hideandseek.HideAndSeekGame
import de.mikeyllp.miniGamesV4.plugin
import de.mikeyllp.miniGamesV4.storage.ClickInviteStorage
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage
import de.mikeyllp.miniGamesV4.utils.ClickInviteUtils
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Consumer

object MenuMain {
    //This method creates a new GUI with the title "Mini Games" and shows it to the player
    fun openGameMenue(player: Player) {
        //Creates a new GUI with 3 rows and the title "Mini Games"
        val gui = ChestGui(3, "MiniGames")

        //This makes that the  Items in the GUI cant be moved
        gui.setOnGlobalClick(Consumer { event: InventoryClickEvent? -> event!!.setCancelled(true) })
        gui.setOnBottomClick(Consumer { event: InventoryClickEvent? -> event!!.setCancelled(true) })
        gui.setOnGlobalDrag(Consumer { event: InventoryDragEvent? -> event!!.setCancelled(true) })

        //Says that all background items can be replaced caus it has the Lowest Priority
        val background = OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST)

        //this is the item that will be used as background
        background.addItem(GuiItem(ItemStack(Material.BLACK_STAINED_GLASS_PANE)))

        //fills all the empty spaces with the background item
        background.setRepeat(true)
        gui.addPane(background)

        val navigatorPane = OutlinePane(3, 1, 3, 1)

        val config = plugin.getConfig()

        // Tic Tac Toe Item
        val tttItem = createGUIItem(Material.PAPER, "TicTacToe")


        // RPS Item
        val rpsItem = createGUIItem(Material.SHEARS, "Schere Stein Papier")

        // HAS Item
        val hasItem = createGUIItem(Material.SPYGLASS, "Hide And Seek")

        //Adds  if it enabled the items to the GUI
        if (config.getBoolean("TicTacToe")) {
            navigatorPane.addItem(GuiItem(tttItem, Consumer { event: InventoryClickEvent? ->
                event!!.setCancelled(true)
                if (event.isShiftClick()) event.setCancelled(true)
                if (event.getClick().isKeyboardClick()) event.setCancelled(true)
                if (canPlay(player, plugin)) return@Consumer
                if (!checkPlayer(player, plugin)) return@Consumer
                ClickInviteUtils.enableClickInvite(player, "TicTacToe")
            }))
        }

        if (config.getBoolean("RockPaperScissors")) {
            navigatorPane.addItem(GuiItem(rpsItem, Consumer { event: InventoryClickEvent? ->
                event!!.setCancelled(true)
                if (event.isShiftClick()) event.setCancelled(true)
                if (event.getClick().isKeyboardClick()) event.setCancelled(true)

                if (canPlay(player, plugin)) return@Consumer
                if (!checkPlayer(player, plugin)) return@Consumer
                ClickInviteUtils.enableClickInvite(player, "RPS")
            }))
        }

        if (config.getBoolean("HideAndSeek")) {
            navigatorPane.addItem(GuiItem(hasItem, Consumer { event: InventoryClickEvent? ->
                event!!.setCancelled(true)
                if (event.isShiftClick()) event.setCancelled(true)
                if (event.getClick().isKeyboardClick()) event.setCancelled(true)
                if (!checkPlayer(player, plugin)) return@Consumer
                val sender = event.getWhoClicked() as Player

                if (InvitePlayerStorage.gameInfo.containsKey(sender)) {
                    MessageUtils.sendAlreadyInGameMessage(sender)
                    player.closeInventory()
                    return@Consumer
                }

                HideAndSeekGame.addPlayerToHAS(sender, plugin)
                Bukkit.getScheduler().runTaskLater(plugin, Runnable { player.closeInventory() }, 2L)
            }))
        }

        gui.addPane(navigatorPane)

        // Shows the GUI to the player
        gui.show(player)
    }

    // This method checks if the player is in the enableListener map and sends a message if he is
    private fun checkPlayer(player: Player, plugin: JavaPlugin): Boolean {
        if (ClickInviteStorage.Companion.enableListener.containsKey(player)) {
            MessageUtils.sendMessage(player, "<red>Du musst erst ein Spieler aussuchen!</red>")
            Bukkit.getScheduler().runTaskLater(plugin, Runnable { player.closeInventory() }, 2L)
            return false
        }
        return true
    }

    private fun createGUIItem(material: Material, name: String): ItemStack {
        val item = ItemStack(material)
        val meta = item.getItemMeta()

        meta.displayName(Component.text(name))
        meta.addEnchant(Enchantment.UNBREAKING, 1, true) // Fake-Enchant
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS) // Nur Glitzern, kein Text

        item.setItemMeta(meta)
        return item
    }

    fun canPlay(player: Player, plugin: JavaPlugin): Boolean {
        if (InvitePlayerStorage.gameInfo.containsKey(player)) {
            MessageUtils.sendAlreadyInGameMessage(player)
            Bukkit.getScheduler().runTaskLater(plugin, Runnable { player.closeInventory() }, 2L)
            return true
        }
        return false
    }
}
