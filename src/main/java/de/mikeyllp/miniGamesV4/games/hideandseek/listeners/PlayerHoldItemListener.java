package de.mikeyllp.miniGamesV4.games.hideandseek.listeners;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

import static de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups.seekerGroup;

public class PlayerHoldItemListener implements Listener {

    private final JavaPlugin plugin;

    public PlayerHoldItemListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerHoldItem(PlayerItemHeldEvent event) {
        FileConfiguration config = plugin.getConfig();
        if (!config.getBoolean("small-modus.is-enabled")) return;
        if (seekerGroup.isEmpty()) return;

        // Checks if the player is a Seeker
        Player player = event.getPlayer();
        for (Map.Entry<String, List<Player>> entry : seekerGroup.entrySet()) {
            List<Player> seekers = entry.getValue();
            if (!seekers.contains(player)) return;
        }

        // If the player hold a puffer fish he will get smaller
        ItemStack hand = player.getInventory().getItem(event.getNewSlot());
        if (hand != null && hand.getType() == Material.PUFFERFISH) {
            player.getAttribute(Attribute.SCALE).setBaseValue(0.001);
        } else {
            player.getAttribute(Attribute.SCALE).setBaseValue(1.0);
        }


    }
}
