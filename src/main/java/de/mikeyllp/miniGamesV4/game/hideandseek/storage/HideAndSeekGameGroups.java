package de.mikeyllp.miniGamesV4.game.hideandseek.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class HideAndSeekGameGroups {
    public static final List<Player> listUntilX = new ArrayList<>();
    public static final List<Player> seekerList = new ArrayList<>();
    public static final List<Player> noMoveList = new ArrayList<>();
    public static final Map<String, List<Player>> noMoveGroup = new HashMap<>();
    public static final Map<String, List<Player>> gameGroup = new HashMap<>();
    public static final Map<String, List<Player>> seekerGroup = new HashMap<>();

    public static final Map<String, HideAndSeekState> gameState = new HashMap<>();

    // Tread-safe counter
    private final AtomicInteger counter = new AtomicInteger(0);
    // Give the time in Milliseconds back
    private final long startTime = System.currentTimeMillis();

    // Method to generate a unique group name
    private String generateGroupName() {
        int count = counter.getAndIncrement();
        return "Group - " + count + " - " + startTime;
    }

    public static void createGroupFromHAS(int playerCount, JavaPlugin plugin) {
        HideAndSeekGameGroups gen = new HideAndSeekGameGroups();
        String groupName = gen.generateGroupName();
        List<Player> playerCopy = new ArrayList<>(listUntilX);
        FileConfiguration config = plugin.getConfig();

        gameGroup.put(groupName, playerCopy);
        listUntilX.clear();

        int i = 0;
        // When there are more players as wish, set the max seekers
        int targetSeekers = calculateSeekers(playerCount);
        if ((config.getInt("maxSeekersPerHASGroup")) != 0 && targetSeekers < config.getInt("maxSeekersPerHASGroup.value")) {
            targetSeekers = config.getInt("maxSeekersPerHASGroup.value");
        }
        // Randomly select a seeker from the group
        while (i < targetSeekers) {
            int randomNumber = (int) (Math.random() * playerCopy.size());
            Player seeker = playerCopy.get(randomNumber);
            if (!seekerList.contains(seeker)) {
                seekerList.add(seeker);
                noMoveList.add(seeker);
                sendCustomMessage(seeker, "Du bist der Sucher!");
                i++;
            }
        }
        // Add the List of the Seekers to the seekerGroup
        List<Player> SeekerCopy = new ArrayList<>(seekerList);
        seekerGroup.put(groupName, SeekerCopy);

        // Add the seekers to the noMoveList
        List<Player> noMoveCopy = new ArrayList<>(noMoveList);
        noMoveGroup.put(groupName, noMoveCopy);

        // Remove seekers from the player list
        seekerList.clear();
        HideAndSeekState state = new HideAndSeekState(groupName, plugin, config.getInt("playTimeHAS"));
        state.hideTime();
        gameState.put(groupName, state);
    }

    // calculate the number of seekers based on the number of players
    static int calculateSeekers(int players) {
        if (players < 20) {
            return 1;
        }
        return (int) Math.ceil(players / 20.0) * 2;
    }
}
