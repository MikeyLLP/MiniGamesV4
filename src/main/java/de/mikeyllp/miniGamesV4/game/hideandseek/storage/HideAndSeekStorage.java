package de.mikeyllp.miniGamesV4.game.hideandseek.storage;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class HideAndSeekStorage {
    public static final List<Player> listUntilX = new ArrayList<>();
    public static final Map<Player, String> gameGroup = new HashMap<>();

    // Tread-safe counter
    private final AtomicInteger counter = new AtomicInteger(0);
    // Give the time in Milliseconds back
    private final long startTime = System.currentTimeMillis();

    // Method to generate a unique group name
    public String generateGroupName() {
        int count = counter.getAndIncrement();
        return "Group - " + count + " - " + startTime;
    }

    // Reset the counter
    public void reset() {
        counter.set(0);
    }
}
