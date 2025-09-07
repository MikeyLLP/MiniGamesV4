package de.mikeyllp.miniGamesV4.games.hideandseek.listeners

import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import de.mikeyllp.miniGamesV4.plugin
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

class PlayerHoldItemListener() : Listener {

    @EventHandler
    fun onPlayerHoldItem(event: PlayerItemHeldEvent) {
        val config = plugin.config

        if (!config.getBoolean("small-modus.is-enabled")) return
        if (HideAndSeekGameGroups.seekerGroup.isEmpty()) return

        val player = event.player
        for (entry in HideAndSeekGameGroups.seekerGroup.entries) {
            val seekers: MutableList<Player> = entry.value
            if (!seekers.contains(player)) return
        }

        val hand = player.inventory.getItem(event.newSlot)
        if (hand != null && hand.type == Material.PUFFERFISH) {
            player.getAttribute(Attribute.SCALE)?.baseValue = 0.001
        } else {
            player.getAttribute(Attribute.SCALE)?.baseValue = 1.0
        }
    }

    @EventHandler
    fun onTrySwitchItem(event: PlayerSwapHandItemsEvent) {
        val config = plugin.getConfig()
        if (!config.getBoolean("small-modus.is-enabled")) return
        if (HideAndSeekGameGroups.seekerGroup.isEmpty()) return

        val player = event.getPlayer()
        for (entry in HideAndSeekGameGroups.seekerGroup.entries) {
            val seekers: MutableList<Player> = entry.value
            if (!seekers.contains(player)) return
        }

        if (event.offHandItem.type == Material.PUFFERFISH) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val config = plugin.getConfig()
        if (!config.getBoolean("small-modus.is-enabled")) return
        if (HideAndSeekGameGroups.seekerGroup.isEmpty()) return

        val player = event.whoClicked as Player
        for (entry in HideAndSeekGameGroups.seekerGroup.entries) {
            val seekers: MutableList<Player> = entry.value
            if (!seekers.contains(player)) return
        }

        if (event.click == ClickType.NUMBER_KEY && (event.currentItem?.type != Material.PUFFERFISH)) {
            event.isCancelled = true
            return
        }

        val item = event.getCurrentItem()
        if (item != null && item.type == Material.PUFFERFISH) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) {
        for (entry in HideAndSeekGameGroups.seekerGroup.entries) {
            val seekers: MutableList<Player> = entry.value
            if (!seekers.contains(event.player)) return
        }

        if (event.itemDrop.itemStack.type == Material.PUFFERFISH) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onItemEat(event: PlayerItemConsumeEvent) {
        for (entry in HideAndSeekGameGroups.seekerGroup.entries) {
            val seekers: MutableList<Player> = entry.value
            if (!seekers.contains(event.player)) return
        }

        if (event.item.type == Material.PUFFERFISH) {
            event.isCancelled = true
        }
    }
}
