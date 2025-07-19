package de.mikeyllp.miniGamesV4.utils

import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import de.mikeyllp.miniGamesV4.games.hideandseek.utils.WaitingForPlayersUtils
import de.mikeyllp.miniGamesV4.games.rps.RPSGame
import de.mikeyllp.miniGamesV4.plugin
import de.mikeyllp.miniGamesV4.storage.ClickInviteStorage
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object clearUtils {
    val confirmClear: MutableMap<CommandSender?, Long?> = HashMap<CommandSender?, Long?>()

    // This is an easy way to clear all the existing lists. I will use this when something is changed in the config or in case of a crash.
    fun clearAllLists(sender: CommandSender) {
        if (!confirmClear.containsKey(sender)) {
            // 10 seconds to confirm the clear command
            confirmClear.put(sender, null)
            MessageUtils.sendMessage(
                sender,
                "<red>Are you sure you want to clear all game lists? Type <gold>/minigames clear</gold> again to confirm.</red>"
            )
            return
        }
        plugin.logger.warning("Clearing all game lists as per request from " + sender.getName())

        for (entry in HideAndSeekGameGroups.Companion.seekerGroup.entries) {
            val seekers: MutableList<Player> = entry.value
            for (seeker in seekers) {
                seeker.getAttribute(Attribute.SCALE)!!.setBaseValue(1.0)
                seeker.getInventory().setItem(plugin.getConfig().getInt("small-modus.slot") - 1, null)
            }
        }

        // All list that need to be cleared
        for (entry in HideAndSeekGameGroups.Companion.gameGroup.entries) {
            val players: MutableList<Player> = entry.value
            val groupName: String? = entry.key
            HideAndSeekGameGroups.Companion.gameState.get(groupName).stopAllTasks()
            for (p in players) {
                HideAndSeekGameGroups.Companion.hiddenNameTag.removeEntry(p.getName())
                p.setAllowFlight(true)
                p.setGlowing(false)
            }
        }
        for (p1 in Bukkit.getOnlinePlayers()) {
            for (p2 in Bukkit.getOnlinePlayers()) {
                if (p1 != p2) p1.showPlayer(plugin, p2)
            }
        }
        HideAndSeekGameGroups.Companion.listUntilX.clear()
        HideAndSeekGameGroups.Companion.gameGroup.clear()
        HideAndSeekGameGroups.Companion.seekerGroup.clear()
        WaitingForPlayersUtils.timerList.clear()
        RPSGame.Companion.inGameStatus.clear()
        RPSGame.Companion.playerGameState.clear()
        RPSGame.Companion.playerGameState.clear()
        ClickInviteStorage.Companion.enableListener.clear()
        ClickInviteStorage.Companion.whatGame.clear()
        InvitePlayerStorage.invites.clear()
        InvitePlayerStorage.invitesTasks.clear()
        InvitePlayerStorage.gameInfo.clear()
        confirmClear.clear()
        HideAndSeekGameGroups.Companion.seekerList.clear()
        HideAndSeekGameGroups.Companion.gameState.clear()
        HideAndSeekGameGroups.Companion.noMoveGroup.clear()


        // Check to Reload the config
        try {
            MessageUtils.reloadConfig()
            plugin.reloadConfig()
            WaitingForPlayersUtils.timerList.clear()

            // Check if the config is current cerated
            if (CheckConfigUtils.checkAndFixingConfig(plugin)) {
                MessageUtils.sendMessage(sender, "Config reloaded successfully!")
                CheckConfigUtils.checkAndFixingConfig(plugin)
                return
            }
            MessageUtils.sendMessage(
                sender,
                "<red> \u26A0 Invalid config.yml detected! The config will be reset to default values and backed up. \u26A0</red>"
            )
        } catch (e: Exception) {
            sender.sendRichMessage(MessageUtils.prefix() + "<red> An error occurred while reloading the config: " + e.message)
        }
    }
}
