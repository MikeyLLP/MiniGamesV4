package de.mikeyllp.miniGamesV4.storage;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class ToggleInvitesStorage implements Listener {

    public static final Map<Player, String> isToggle = new HashMap<>();

    //Adds or removes the player from the toggle list
    public static void addToggle(Player player) {
        if (isToggle.containsKey(player)) {
            sendCustomMessage(player, "<color:#00E5E5>Du kannst jetzt wieder eingeladen werden!</color:#00E5E5>");
            isToggle.remove(player);
            return;
        }
        sendCustomMessage(player, "<color:#00E5E5>Du kannst jetzt nicht mehr eingeladen werden!</color:#00E5E5>");
        isToggle.put(player, "placeHolder");

    }

    //Checks if the player is in the toggle list and removes the Player if he Quits
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (isToggle.containsKey(player)) {
            isToggle.remove(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("minigamesv4.autotoggle")) {
            addToggle(player);
        }
    }
}
