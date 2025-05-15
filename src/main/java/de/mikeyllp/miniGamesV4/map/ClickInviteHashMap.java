package de.mikeyllp.miniGamesV4.map;

import de.mikeyllp.miniGamesV4.MiniGamesV4;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class ClickInviteHashMap implements Listener {

    public static final Map<Player, BukkitTask> enableListener = new HashMap<>();
    private static final Map<Player, String> whatGame = new HashMap<>();

    //adds the Player to the enable Listener Map and starts a 60 second timer after the timer ends the Player will be removed from the Map
    public static void addEnableListener(Player player, String miniGame) {
        enableListener.put(player, new BukkitRunnable() {
            @Override
            public void run() {
                enableListener.remove(player);
            }
        }.runTaskLater(MiniGamesV4.getInstance(), 1200L));
        whatGame.put(player, miniGame);
    }


    //Könne man in eine Metohde ändern
    //Checks if the Player is in the enable Listener Map and if he is it will remove him from the Map and perform the command
    @EventHandler
    public void onPlayerRightClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            playerGotClicked(event.getPlayer(), (Player) event.getRightClicked());
        }
    }

    @EventHandler
    public void onPlayerLeftClick(PrePlayerAttackEntityEvent event) {
        if (event.getAttacked() instanceof Player) {
            playerGotClicked(event.getPlayer(), (Player) event.getAttacked());
        }
    }

    private static void playerGotClicked(Player inviter, Player invited) {
        if (enableListener.containsKey(inviter)) {
            inviter.performCommand("minigames " + whatGame.get(inviter) + " " + invited.getName());
            enableListener.remove(inviter);
            whatGame.remove(inviter);
        }
    }
}
