package de.mikeyllp.miniGamesV4.games.hideandseek.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static de.mikeyllp.miniGamesV4.games.hideandseek.HideAndSeekGame.startGame;
import static de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups.listUntilX;
import static de.mikeyllp.miniGamesV4.games.hideandseek.utils.formatTimeUtils.formatTimer;

public class WaitingForPlayersUtils {
    public static BukkitRunnable waitingTask;

    public static final List<Integer> timerList = new ArrayList<>();
    private static boolean timerRunning = false;

    public static void startWaitingTask(JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        int rawTimer = config.getInt("timeAutoStartHASGroup");
        int maxPlayers = config.getInt("maxPlayersPerHASGroup");
        int minPlayers = config.getInt("minPlayersPerHASGroup");
        MiniMessage mm = MiniMessage.miniMessage();

        // Create a new task
        waitingTask = new BukkitRunnable() {
            // This is for the timer countdown
            boolean countdownStarted = false;
            int timer = rawTimer;

            @Override
            public void run() {
                int currentSize = listUntilX.size();
                // Check if the there are no players in the list if so cancel the task
                if (currentSize == 0) {
                    cancel();
                    waitingTask = null;
                    timerRunning = false;
                    countdownStarted = false;
                    return;
                }
                // Checks if enough players are in the list, if not cancel the timer
                if (listUntilX.size() < minPlayers && timerRunning) {
                    for(Player p : listUntilX) {
                        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                    }
                    timerRunning = false;
                    countdownStarted = false;
                    int timer = rawTimer;
                    return;
                }
                // If the countdown has not started and enough players are in the list, start the countdown
                if (!countdownStarted && currentSize >= minPlayers) {
                    countdownStarted = true;
                    timerRunning = true;
                    int timer = rawTimer;
                }

                // This is the countdown logic, if the countdown has started, send the action bar message
                if (countdownStarted) {
                    String showTimer = formatTimer(timer);
                    for (Player p : listUntilX) {
                        p.sendActionBar(mm.deserialize("<gold>Spiel startet in: <color:#00E5E5>" + showTimer +
                                "</color> (<color:#00E5E5>" + currentSize + "</color>/<color:#00E5E5>" + maxPlayers + "</color>)</gold>"));
                        if (timer <= 5) {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                        }
                    }
                    if (timer <= 0 || currentSize >= maxPlayers) {
                        for (Player p : listUntilX) {
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.5F);
                        }
                        startGame(plugin, config);
                        cancel();
                        waitingTask = null;
                        timerRunning = false;
                    }
                    timer--;
                } else {
                    for (Player p : listUntilX) {
                        p.sendActionBar(mm.deserialize("<gold>Warten auf weitere Spieler... (<color:#00E5E5>" + currentSize + "</color>/<color:#00E5E5>" + maxPlayers + "</color>)</gold>"));
                    }
                }
            }
        };
        waitingTask.runTaskTimer(plugin, 0L, 20L);
    }
}
