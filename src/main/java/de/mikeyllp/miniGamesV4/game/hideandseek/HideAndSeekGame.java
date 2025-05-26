package de.mikeyllp.miniGamesV4.game.hideandseek;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekStorage.*;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class HideAndSeekGame {
    private static int index = 0;
    private static BukkitRunnable waitingTask;

    public static void addPlayerToHAS(Player player, JavaPlugin plugin) {
        if (listUntilX.contains(player)) {
            sendCustomMessage(player, "<red>Du bist bereits in der Warteschlange!");
            return;
        }
        for (String s : gameGroup.keySet()) {
            if (gameGroup.get(s).contains(player)) {
                sendCustomMessage(player, "<red>Du bist bereits in einem Spiel!");
                return;
            }
        }

        listUntilX.add(player);
        if (waitingTask == null) {
            startWaitingTask(plugin);
        }
    }

    public static final List<String> timerList = new ArrayList<>();


    public static void startWaitingTask(JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        int timer = config.getInt("timeAutoStartHASGroup");


        timerList.clear();
        for (int i = timer; i > 0; i--) {
            timerList.add(String.valueOf(i));
        }
        index = 0;

        waitingTask = new BukkitRunnable() {
            @Override
            public void run() {

                int maxPlayers = config.getInt("maxPlayersPerHASGroup");
                int minPlayers = config.getInt("minPlayersPerHASGroup");

                MiniMessage mm = MiniMessage.miniMessage();

                // The Actionbar for the players in the waiting list
                for (Player p : listUntilX) {
                    p.sendActionBar(mm.deserialize("<gold>Warten auf weitere Spieler... (<color:#00E5E5>" + listUntilX.size() + "</color>/<color:#00E5E5>" + maxPlayers + "</color>)</gold>"));
                }

                // When the min players is reached, the timer starts
                if (listUntilX.size() >= minPlayers) {
                    for (Player p : listUntilX) {
                        p.sendActionBar(mm.deserialize("<gold>Spiel startet in: <color:#00E5E5>" + timerList.get(index) + "</color> (<color:#00E5E5>" + listUntilX.size() + "</color>/<color:#00E5E5>" + maxPlayers + "</color>)</gold>"));
                    }
                    index++;
                    if (index >= timerList.size()) {
                        // If the timer reaches 0, the game starts
                        Location loc = new Location(
                                plugin.getServer().getWorld(config.getString("spawn-location.world")),
                                config.getDouble("spawn-location.x"),
                                config.getDouble("spawn-location.y"),
                                config.getDouble("spawn-location.z")
                        );
                        for (Player p : listUntilX) {
                            p.teleportAsync(loc);
                        }
                        createGroupFromHAS(listUntilX.size(), plugin);
                        cancel();
                        waitingTask = null;
                    }
                }

                // Starts the timer directly if the max players is reached
                if (listUntilX.size() >= maxPlayers) {
                    Location loc = new Location(
                            plugin.getServer().getWorld(config.getString("spawn-location.world")),
                            config.getDouble("spawn-location.x"),
                            config.getDouble("spawn-location.y"),
                            config.getDouble("spawn-location.z")
                    );
                    for (Player p : listUntilX) {
                        p.teleportAsync(loc);
                    }
                    createGroupFromHAS(listUntilX.size(), plugin);
                    cancel();
                    waitingTask = null;
                }
            }
        };
        waitingTask.runTaskTimer(plugin, 0L, 20L);
    }
}
