package de.mikeyllp.miniGamesV4.game.hideandseek.storage;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekGameGroups.*;
import static de.mikeyllp.miniGamesV4.game.hideandseek.utils.formatTimeUtils.formatTimer;
import static de.mikeyllp.miniGamesV4.game.hideandseek.utils.removePlayersHideAndSeek.playerRemove;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class HideAndSeekState {
    // Groups
    private final String groupName;
    private final List<Player> groupList;
    private final List<Player> seekerList;
    private final List<Player> noMoveList;

    // Timer
    private int hideTime;
    private int searchTime;

    // Task
    private BukkitRunnable hideTask;
    private BukkitRunnable inGameTask;

    // Plugin instance
    private final JavaPlugin plugin;

    // State of the game
    public HideAndSeekState(String groupName, JavaPlugin plugin, int time) {
        this.groupName = groupName;
        this.plugin = plugin;
        this.searchTime = time;

        this.groupList = gameGroup.get(groupName);
        this.seekerList = seekerGroup.getOrDefault(groupName, new ArrayList<>());
        this.noMoveList = noMoveGroup.getOrDefault(groupName, new ArrayList<>());
    }

    public void hideTime() {
        FileConfiguration config = plugin.getConfig();
        int hideTimerFirst = config.getInt("hideTimeHAS");

        MiniMessage mm = MiniMessage.miniMessage();

        // disable flight for all players in the group
        for (Player p : groupList) {
            p.setAllowFlight(false);
        }

        hideTask = new BukkitRunnable() {
            int hideTimeLeft = hideTimerFirst;

            @Override
            public void run() {

                String hideTimer = formatTimer(hideTimeLeft);
                // Show the action bar message to all players in the group
                for (Player p : groupList) {
                    p.sendActionBar(mm.deserialize("<gold>Die sucher können suchen in: <color:#00E5E5>" + hideTimer + "</color>"));
                }

                // Give the seekers a blindness effect
                for (Player p : seekerList) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 100, false, false, false));
                }

                // If the timer has reached 0, start the game
                if (hideTimeLeft <= 0) {
                    noMoveGroup.remove(groupName);
                    startGameTask();
                    cancel();
                }
                hideTimeLeft--;
            }
        };
        hideTask.runTaskTimer(plugin, 0L, 20L);
    }

    public void startGameTask() {
        FileConfiguration config = plugin.getConfig();

        int timerFirst = config.getInt("playTimeHAS");

        MiniMessage mm = MiniMessage.miniMessage();


        inGameTask = new BukkitRunnable() {
            int timeLeft = timerFirst;


            @Override
            public void run() {
                int hidersSize = groupList.size() - seekerList.size();
                String timer = formatTimer(timeLeft);

                // Show the action bar message to all players in the group
                for (Player p : groupList) {
                    p.sendActionBar(mm.deserialize("<gold>Das Spiel endet in: <color:#00E5E5>" + timer + "</color> Übrige Verstecker <color:#00E5E5>" + hidersSize + "</color></gold>"));
                }


                // Checks if the timer has reached 0
                if (timeLeft <= 0) {
                    for (Player p : groupList) {
                        sendCustomMessage(p, "Die Verstecker haben gewonnen!");
                    }
                    for (Player p : new ArrayList<>(groupList)) {
                        playerRemove(p, "gameEnd", plugin);
                    }
                    gameState.remove(groupName);
                    cancel();
                    return;
                }


                // If there are no seekers left
                if (seekerList.isEmpty()) {
                    for (Player p : groupList) {
                        sendCustomMessage(p, "Die Verstecker haben gewonnen!");
                    }
                    for (Player p : new ArrayList<>(groupList)) {
                        playerRemove(p, "gameEnd", plugin);
                    }
                    cancel();
                    return;
                }

                // If all hiders are found
                if (seekerList.size() >= groupList.size()) {
                    for (Player p : seekerList) {
                        sendCustomMessage(p, "Die Sucher haben gewonnen!");
                    }
                    for (Player p : new ArrayList<>(groupList)) {
                        playerRemove(p, "gameEnd", plugin);
                    }
                    gameState.remove(groupName);
                    cancel();
                    return;
                }
                timeLeft--;
            }
        };
        inGameTask.runTaskTimer(plugin, 0L, 20L);
    }
}