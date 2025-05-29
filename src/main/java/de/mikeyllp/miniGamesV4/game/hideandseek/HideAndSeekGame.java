package de.mikeyllp.miniGamesV4.game.hideandseek;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekStorage.createGroupFromHAS;
import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekStorage.listUntilX;
import static de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.gameInfo;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendAlreadyInGameMessage;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class HideAndSeekGame {
    private static int index = 0;
    private static BukkitRunnable waitingTask;

    public static void addPlayerToHAS(Player player, JavaPlugin plugin) {
        if (listUntilX.contains(player)) {
            sendCustomMessage(player, "<red>Du bist bereits in der Warteschlange!");
            return;
        }

        if (gameInfo.containsKey(player)) {
            sendAlreadyInGameMessage(player);
            return;
        }

        listUntilX.add(player);
        gameInfo.put(player, player);
        if (waitingTask == null) {
            startWaitingTask(plugin);
        }
    }

    public static final List<String> timerList = new ArrayList<>();
    private static boolean timerRunning = false;

    public static void startWaitingTask(JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        int timer = config.getInt("timeAutoStartHASGroup");
        int maxPlayers = config.getInt("maxPlayersPerHASGroup");
        int minPlayers = config.getInt("minPlayersPerHASGroup");
        MiniMessage mm = MiniMessage.miniMessage();


        waitingTask = new BukkitRunnable() {
            int index = 0;
            boolean countdownStarted = false;

            @Override
            public void run() {
                int currentSize = listUntilX.size();
                if (currentSize == 0) {
                    cancel();
                    waitingTask = null;
                    timerRunning = false;
                    countdownStarted = false;
                    return;
                }

                if (listUntilX.size() < minPlayers && timerRunning) {
                    timerRunning = false;
                    countdownStarted = false;
                    index = 0;
                    timerList.clear();
                    return;
                }

                if (!countdownStarted && currentSize >= minPlayers) {
                    countdownStarted = true;
                    timerRunning = true;
                    timerList.clear();
                    for (int i = timer; i > 0; i--) {
                        timerList.add(String.valueOf(i));
                    }
                    index = 0;
                }

                if (countdownStarted) {
                    for (Player p : listUntilX) {
                        p.sendActionBar(mm.deserialize("<gold>Spiel startet in: <color:#00E5E5>" + timerList.get(index) + "</color> (<color:#00E5E5>" + currentSize + "</color>/<color:#00E5E5>" + maxPlayers + "</color>)</gold>"));
                    }
                    index++;
                    if (index >= timerList.size() || currentSize >= maxPlayers) {
                        startGame(plugin, config);
                        cancel();
                        waitingTask = null;
                        timerRunning = false;
                    }
                } else {
                    for (Player p : listUntilX) {
                        p.sendActionBar(mm.deserialize("<gold>Warten auf weitere Spieler... (<color:#00E5E5>" + currentSize + "</color>/<color:#00E5E5>" + maxPlayers + "</color>)</gold>"));
                    }
                }
            }
        };
        waitingTask.runTaskTimer(plugin, 0L, 20L);
    }

    public static void startGame(JavaPlugin plugin, FileConfiguration config) {
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
    }


}
