package de.mikeyllp.miniGamesV4.game.hideandseek;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekGameGroups.createGroupFromHAS;
import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekGameGroups.listUntilX;
import static de.mikeyllp.miniGamesV4.game.hideandseek.utils.WaitingForPlayersUtils.startWaitingTask;
import static de.mikeyllp.miniGamesV4.game.hideandseek.utils.WaitingForPlayersUtils.waitingTask;
import static de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.gameInfo;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendAlreadyInGameMessage;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class HideAndSeekGame {


    public static void addPlayerToHAS(Player player, JavaPlugin plugin) {
        if (listUntilX.contains(player)) {
            sendCustomMessage(player, "<red>Du bist bereits in der Warteschlange!");
            return;
        }

        if (gameInfo.containsKey(player)) {
            sendAlreadyInGameMessage(player);
            return;
        }

        // Add the player to the list so that they can´t be invited to another game
        listUntilX.add(player);
        gameInfo.put(player, player);
        if (waitingTask == null) {
            startWaitingTask(plugin);
        }
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
