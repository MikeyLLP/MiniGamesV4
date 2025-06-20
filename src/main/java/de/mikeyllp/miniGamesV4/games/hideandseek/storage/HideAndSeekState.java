package de.mikeyllp.miniGamesV4.games.hideandseek.storage;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups.*;
import static de.mikeyllp.miniGamesV4.games.hideandseek.utils.formatTimeUtils.formatTimer;
import static de.mikeyllp.miniGamesV4.games.hideandseek.utils.removePlayersHideAndSeek.playerRemove;
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
        // Set the health max
        for (Player p : groupList) {
            p.setAllowFlight(false);
            p.setFoodLevel(20);
            p.setHealth(20);
        }

        hideTask = new BukkitRunnable() {
            int hideTimeLeft = hideTimerFirst;

            @Override
            public void run() {
                String hideTimer = formatTimer(hideTimeLeft);
                // Show the action bar message to all players in the group
                for (Player p : groupList) {
                    p.sendActionBar(mm.deserialize("<gold>Die sucher können suchen in: <color:#00E5E5>" + hideTimer + "</color>"));
                    if (hideTimeLeft <= 5) {
                        if (hideTimeLeft == 0) {
                            sendCustomMessage(p, "Spiel startet in: <gold>Jetzt!</gold>");
                        } else {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                            sendCustomMessage(p, "Spiel startet in: <gold>" + hideTimeLeft + "</gold>");
                        }
                    }
                }

                // Give the seekers a blindness effect
                for (Player p : seekerList) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 100, false, false, false));
                }

                // If the timer has reached 0, start the game
                if (hideTimeLeft <= 0) {
                    for (Player p : noMoveGroup.get(groupName)) {
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.5F);
                    }
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

        // hints
        int timerFirst = config.getInt("playTimeHAS");
        int hints = config.getInt("HASHints");
        int hintsTime = config.getInt("HintTimeHAS");

        final List<Integer> hintTimes = new ArrayList<>();
        final List<Integer> hintRemove = new ArrayList<>();

        MiniMessage mm = MiniMessage.miniMessage();

        // To know how long the interval is between the hints
        long intervallSek = timerFirst / hints;
        int hintTime = Math.round(intervallSek);

        for (int i = 0; i <= hints; i++) {
            int someHint = hintTime * i;
            hintTimes.add(someHint);
            hintRemove.add(someHint + hintsTime);
        }

        inGameTask = new BukkitRunnable() {
            int timeLeft = timerFirst;


            @Override
            public void run() {
                int hidersSize = groupList.size() - seekerList.size();
                String timer = formatTimer(timeLeft);

                // Here we check if the timeLeft is in the hintTimes list to set the glowing effect
                if (hintTimes.contains(timeLeft)) {
                    for (Player p : groupList) {
                        p.setGlowing(true);
                    }
                }

                // Here we check if the timeLeft is in the hintRemove list to remove the glowing effect
                if (hintRemove.contains(timeLeft)) {
                    for (Player p : groupList) {
                        p.setGlowing(false);
                    }
                }

                // Show the action bar message to all players in the group
                for (Player p : groupList) {
                    p.sendActionBar(mm.deserialize("<gold>Das Spiel endet in: <color:#00E5E5>" + timer + "</color> Übrige Verstecker <color:#00E5E5>" + hidersSize + "</color></gold>"));
                    if (timeLeft == 60) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                        sendCustomMessage(p, "Noch <gold>60</gold> Sekunden ");
                    }
                    if (timeLeft == 30) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                        sendCustomMessage(p, "<gold>30</gold> Sekunden");
                    }
                    if (timeLeft == 15) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                        sendCustomMessage(p, "Noch <gold>15</gold> Sekunden");
                    }
                    if (timeLeft <= 5) {
                        if (timeLeft == 0) {
                            sendCustomMessage(p, "Spiel endet in: <gold>Jetzt!</gold>");
                        } else {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                            sendCustomMessage(p, "Spiel endet in: <gold>" + timeLeft + "</gold>");
                        }
                    }
                }


                // Checks if the timer has reached 0
                if (timeLeft <= 0) {
                    for (Player p : groupList) {
                        if (seekerList.contains(p)) {
                            Location seekerLoc = p.getLocation();
                            p.playSound(seekerLoc, Sound.ENTITY_VILLAGER_DEATH, 1.0F, 1.0F);
                            sendCustomMessage(p, "Die Verstecker haben gewonnen!");
                            continue;
                        }
                        Location hiderLoc = p.getLocation();
                        p.playSound(hiderLoc, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                        sendCustomMessage(p, "Die Verstecker haben gewonnen!");

                    }
                    for (Player p : new ArrayList<>(groupList)) {
                        playerRemove(p, "gameEnd", plugin);
                    }
                    cancel();
                    return;
                }


                // If there are no seekers left
                if (seekerList.isEmpty()) {
                    for (Player p : groupList) {
                        if (seekerList.contains(p)) {
                            p.playSound( p.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1.0F, 1.0F);
                            sendCustomMessage(p, "Die Verstecker haben gewonnen!");
                            continue;
                        }
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
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
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
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

    public void stopAllTasks() {
        if (hideTask != null) {
            hideTask.cancel();
            hideTask = null;
        }
        if (inGameTask != null) {
            inGameTask.cancel();
            inGameTask = null;
        }
    }
}