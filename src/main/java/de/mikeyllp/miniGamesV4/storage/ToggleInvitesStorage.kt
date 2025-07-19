package de.mikeyllp.miniGamesV4.storage

import de.mikeyllp.miniGamesV4.MiniGamesV4
import de.mikeyllp.miniGamesV4.database.Database
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.function.Consumer

class ToggleInvitesStorage(private val db: Database) {
    fun checkToggle(player: Player, callback: Consumer<Boolean>) {
        val playerUuid = player.uniqueId
        db.getToggleAsync(playerUuid).thenAccept(Consumer { toggle: Int ->
            // This is a short way to check if its 0 or 1
            val result = toggle == 0
            Bukkit.getScheduler().runTask(MiniGamesV4.Companion.instance!!, Runnable {
                callback.accept(result)
            })
        })
    }


    //Adds or removes the player from the toggle list
    fun addToggle(player: Player) {
        val playerUuid = player.uniqueId
        db.getToggleAsync(playerUuid).thenAccept(Consumer { toggle: Int? ->
            // This is if or else in a single line
            val newToggle = if (toggle == 1) 0 else 1
            db.setToggleAsync(playerUuid, newToggle).thenRun(Runnable {
                Bukkit.getScheduler().runTask(
                    MiniGamesV4.Companion.instance!!,
                    Runnable {
                        if (newToggle == 1) {
                            MessageUtils.sendMessage(
                                player,
                                "<color:#00E5E5>Du kannst jetzt nicht mehr eingeladen werden!</color:#00E5E5>"
                            )
                        } else {
                            MessageUtils.sendMessage(
                                player,
                                "<color:#00E5E5>Du kannst jetzt wieder eingeladen werden!</color:#00E5E5>"
                            )
                        }
                    })
            })
        })
    }
}
