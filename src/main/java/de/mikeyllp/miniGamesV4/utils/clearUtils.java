package de.mikeyllp.miniGamesV4.utils;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups.*;
import static de.mikeyllp.miniGamesV4.games.hideandseek.utils.WaitingForPlayersUtils.timerList;
import static de.mikeyllp.miniGamesV4.games.rps.RPSGame.inGameStatus;
import static de.mikeyllp.miniGamesV4.games.rps.RPSGame.playerGameState;
import static de.mikeyllp.miniGamesV4.storage.ClickInviteStorage.enableListener;
import static de.mikeyllp.miniGamesV4.storage.ClickInviteStorage.whatGame;
import static de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.*;
import static de.mikeyllp.miniGamesV4.storage.ToggleInvitesStorage.isToggle;
import static de.mikeyllp.miniGamesV4.utils.CheckConfigUtils.checkAndFixingConfig;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.*;

public class clearUtils {

    public static final Map<CommandSender, Long> confirmClear = new HashMap<>();

    // This is an easy way to clear all the existing lists. I will use this when something is changed in the config or in case of a crash.
    public static void clearAllLists(CommandSender sender, JavaPlugin plugin) {
        if (!confirmClear.containsKey(sender)) {
            // 10 seconds to confirm the clear command
            confirmClear.put(sender, null);
            sendCustomMessage(sender, "<red>Are you sure you want to clear all game lists? Type <gold>/minigames clear</gold> again to confirm.</red>");
            return;
        }
        plugin.getLogger().warning("Clearing all game lists as per request from " + sender.getName());

        for (Map.Entry<String, List<Player>> entry : seekerGroup.entrySet()) {
            List<Player> seekers = entry.getValue();
            for (Player seeker :seekers) {
                seeker.getAttribute(Attribute.SCALE).setBaseValue(1.0);
                seeker.getInventory().setItem(plugin.getConfig().getInt("small-modus.slot") - 1, null);
            }
        }

        // All list that need to be cleared
        for (Map.Entry<String, List<Player>> entry : gameGroup.entrySet()) {
            List<Player> players = entry.getValue();
            String groupName = entry.getKey();
            gameState.get(groupName).stopAllTasks();
            for (Player p : players) {
                hiddenNameTag.removeEntry(p.getName());
                p.setAllowFlight(true);
                p.setGlowing(false);
            }
        }
        for (Player p1 : Bukkit.getOnlinePlayers()) {
            for (Player p2 : Bukkit.getOnlinePlayers()) {
                if (!p1.equals(p2)) p1.showPlayer(plugin, p2);
            }
        }
        listUntilX.clear();
        gameGroup.clear();
        seekerGroup.clear();
        timerList.clear();
        inGameStatus.clear();
        playerGameState.clear();
        playerGameState.clear();
        enableListener.clear();
        whatGame.clear();
        invitesManager.clear();
        invitesTasks.clear();
        gameInfo.clear();
        isToggle.clear();
        confirmClear.clear();
        seekerList.clear();
        gameState.clear();
        noMoveGroup.clear();





        // Check to Reload the config
        try {
            reloadConfig();
            plugin.reloadConfig();
            timerList.clear();

            // Check if the config is current cerated
            if (checkAndFixingConfig(plugin)) {
                sendCustomMessage(sender, "Config reloaded successfully!");
                checkAndFixingConfig(plugin);
                return;
            }
            sendCustomMessage(sender, "<red> \u26A0 Invalid config.yml detected! The config will be reset to default values and backed up. \u26A0</red>");

        } catch (Exception e) {
            sender.sendRichMessage(prefix() + "<red> An error occurred while reloading the config: " + e.getMessage());
        }
    }
}
