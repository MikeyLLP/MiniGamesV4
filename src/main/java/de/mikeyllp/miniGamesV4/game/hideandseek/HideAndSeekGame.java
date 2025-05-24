package de.mikeyllp.miniGamesV4.game.hideandseek;

import de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekStorage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekStorage.gameGroup;
import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekStorage.listUntilX;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class HideAndSeekGame {
    public static void addPlayerToHAS(Player player, JavaPlugin plugin) {
        if (listUntilX.contains(player)) {
            sendCustomMessage(player, "<red>Du bist bereits in der Warteschlange!");
            return;
        }
        listUntilX.add(player);

        int maxPlayers = plugin.getConfig().getInt("maxPlayersPerHASGroup") - 1;
        // Get the max players from the config file
        if (listUntilX.size() == maxPlayers) {
            createGroupFromHAS();
        }
    }

    private static void createGroupFromHAS() {
        HideAndSeekStorage gen = new HideAndSeekStorage();
        String groupName = gen.generateGroupName();
        for (Player p : listUntilX) {
            listUntilX.remove(p);
            gameGroup.put(p, groupName);
        }
    }
}
