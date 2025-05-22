package de.mikeyllp.miniGamesV4.storage;

import de.mikeyllp.miniGamesV4.MiniGamesV4;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static de.mikeyllp.miniGamesV4.utils.ClickInviteUtils.playerGotClicked;
import static de.mikeyllp.miniGamesV4.utils.ClickInviteUtils.removePlayer;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class ClickInviteStorage implements Listener {

    public static final Map<Player, BukkitTask> enableListener = new HashMap<>();
    public static final Map<Player, String> whatGame = new HashMap<>();

    // Adds the Player to the enable Listener Map and starts a 60 second timer after the timer ends the Player will be removed from the Map
    public static void addEnableListener(Player player, String miniGame) {
        enableListener.put(player, new BukkitRunnable() {
            @Override
            public void run() {
                removePlayer(player);
                sendCustomMessage(player, "<red>Du kannst keine Spieler mehr einladen.</red>");
            }
        }.runTaskLater(MiniGamesV4.getInstance(), 1200L));
        whatGame.put(player, miniGame);
    }


    // Checks if the Player is in the enable Listener Map and if he is it will remove him from the Map and perform the command
    @EventHandler
    public void onPlayerRightClick(PlayerInteractEntityEvent event) {
        if (enableListener.containsKey(event.getPlayer())) {
            if (event.getRightClicked() instanceof Player) {
                playerGotClicked(event.getPlayer(), (Player) event.getRightClicked());
            }
        }
    }

    @EventHandler
    public void onPlayerLeftClick(PrePlayerAttackEntityEvent event) {
        if (enableListener.containsKey(event.getPlayer())) {
            if (event.getAttacked() instanceof Player) {
                playerGotClicked(event.getPlayer(), (Player) event.getAttacked());
            }
        }
    }

    // Removes the Player from the enable Listener Map when he quits
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (enableListener.containsKey(event.getPlayer())) {
            removePlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerMessage(AsyncChatEvent event) {
        if (enableListener.containsKey(event.getPlayer())) {
            Player inviter = event.getPlayer();
            String msg = PlainTextComponentSerializer.plainText().serialize(event.message()).trim().toLowerCase();
            if (msg.equals("cancel")) {
                event.setCancelled(true);
                MiniMessage mm = MiniMessage.miniMessage();
                Component miniGameComponent = mm.deserialize("<gold>Click Invite<gold> <red>Disabled</red>");
                Component message = mm.deserialize("");
                inviter.closeInventory();
                inviter.showTitle(Title.title(miniGameComponent,
                        message, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))));
                removePlayer(inviter);
            }
        }
    }
}
