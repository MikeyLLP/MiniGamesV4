package de.mikeyllp.miniGamesV4.games.hideandseek.listeners

import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.plugin.java.JavaPlugin

class PlayerHoldItemListener(private val plugin: JavaPlugin) : Listener {
    @EventHandler
    fun onPlayerHoldItem(event: PlayerItemHeldEvent) {
        val config = plugin.getConfig()
        if (!config.getBoolean("small-modus.is-enabled")) return
        if (HideAndSeekGameGroups.Companion.seekerGroup.isEmpty()) return

        // Checks if the player is a Seeker
        val player = event.getPlayer()
        for (entry in HideAndSeekGameGroups.Companion.seekerGroup.entries) {
            val seekers: MutableList<Player?> = entry.value
            if (!seekers.contains(player)) return
        }

        // If the player hold a puffer fish he will get smaller
        val hand = player.getInventory().getItem(event.getNewSlot())
        if (hand != null && hand.getType() == Material.PUFFERFISH) {
            player.getAttribute(Attribute.SCALE)!!.setBaseValue(0.001)
        } else {
            player.getAttribute(Attribute.SCALE)!!.setBaseValue(1.0)
        }
    }

    // That the seeker cannot swap the item
    @EventHandler
    fun onTrySwitchItem(event: PlayerSwapHandItemsEvent) {
        val config = plugin.getConfig()
        if (!config.getBoolean("small-modus.is-enabled")) return
        if (HideAndSeekGameGroups.Companion.seekerGroup.isEmpty()) return

        // Checks if the player is a Seeker
        val player = event.getPlayer()
        for (entry in HideAndSeekGameGroups.Companion.seekerGroup.entries) {
            val seekers: MutableList<Player?> = entry.value
            if (!seekers.contains(player)) return
        }

        if (event.getOffHandItem().getType() == Material.PUFFERFISH) {
            event.setCancelled(true)
        }
    }

    // That the player cannot move his item and duplicate it
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val config = plugin.getConfig()
        if (!config.getBoolean("small-modus.is-enabled")) return
        if (HideAndSeekGameGroups.Companion.seekerGroup.isEmpty()) return

        // Checks if the player is a Seeker
        val player = event.getWhoClicked() as Player
        for (entry in HideAndSeekGameGroups.Companion.seekerGroup.entries) {
            val seekers: MutableList<Player?> = entry.value
            if (!seekers.contains(player)) return
        }

        if (event.getClick() == ClickType.NUMBER_KEY && (event.getCurrentItem()!!.getType() != Material.PUFFERFISH)) {
            event.setCancelled(true)
            return
        }

        val item = event.getCurrentItem()
        if (item != null && item.getType() == Material.PUFFERFISH) {
            event.setCancelled(true)
        }
    }

    // That the seeker cannot drop his item
    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) {
        for (entry in HideAndSeekGameGroups.Companion.seekerGroup.entries) {
            val seekers: MutableList<Player?> = entry.value
            if (!seekers.contains(event.getPlayer())) return
        }

        if (event.getItemDrop().getItemStack().getType() == Material.PUFFERFISH) {
            event.setCancelled(true)
        }
    }

    // That the seeker cannot eat his item
    @EventHandler
    fun onItemEat(event: PlayerItemConsumeEvent) {
        for (entry in HideAndSeekGameGroups.Companion.seekerGroup.entries) {
            val seekers: MutableList<Player?> = entry.value
            if (!seekers.contains(event.getPlayer())) return
        }

        if (event.getItem().getType() == Material.PUFFERFISH) {
            event.setCancelled(true)
        }
    }
}
