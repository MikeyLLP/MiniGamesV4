package de.mikeyllp.miniGamesV4.utils

import de.mikeyllp.miniGamesV4.storage.ClickInviteStorage
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.time.Duration

object ClickInviteUtils {
    // Shows the Title to the Player and add the Listener
    fun enableClickInvite(player: Player, miniGame: String?) {
        ClickInviteStorage.Companion.addEnableListener(player, miniGame)
        val mm = MiniMessage.miniMessage()
        val miniGameComponent = mm.deserialize("<gold>Click Invite<gold> <green>Enabled</green>")
        val message =
            mm.deserialize("<color:#00E5E5>Spieler anklicken.</color:#00E5E5> <red>'/minigames quit' zum Abbrechen.</red>")
        player.closeInventory()
        player.showTitle(
            Title.title(
                miniGameComponent,
                message, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
            )
        )
    }

    // Sends the player an invite if they right- or left-click on another player.
    fun playerGotClicked(inviter: Player, invited: Player) {
        inviter.performCommand("minigames " + ClickInviteStorage.Companion.whatGame.get(inviter) + " " + invited.getName())
        removePlayer(inviter)
    }

    // Removes the Player from the enable Listener Map
    fun removePlayer(inviter: Player?) {
        val task: BukkitTask? = ClickInviteStorage.Companion.enableListener.get(inviter)
        if (task != null) {
            task.cancel()
            ClickInviteStorage.Companion.enableListener.remove(inviter)
        }
        ClickInviteStorage.Companion.whatGame.remove(inviter)
    }
}
