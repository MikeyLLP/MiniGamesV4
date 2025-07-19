package de.mikeyllp.miniGamesV4.games.hideandseek.utils

import de.mikeyllp.miniGamesV4.games.hideandseek.HideAndSeekGame
import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Sound
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

object WaitingForPlayersUtils {
    var waitingTask: BukkitRunnable? = null

    val timerList: MutableList<Int?> = ArrayList<Int?>()
    private var timerRunning = false

    fun startWaitingTask(plugin: JavaPlugin) {
        val config = plugin.getConfig()
        val rawTimer = config.getInt("timeAutoStartHASGroup")
        val maxPlayers = config.getInt("maxPlayersPerHASGroup")
        val minPlayers = config.getInt("minPlayersPerHASGroup")
        val mm = MiniMessage.miniMessage()

        // Create a new task
        waitingTask = object : BukkitRunnable() {
            // This is for the timer countdown
            var countdownStarted: Boolean = false
            var timer: Int = rawTimer

            override fun run() {
                val currentSize: Int = HideAndSeekGameGroups.Companion.listUntilX.size
                // Check if the there are no players in the list if so cancel the task
                if (currentSize == 0) {
                    cancel()
                    waitingTask = null
                    timerRunning = false
                    countdownStarted = false
                    return
                }
                // Checks if enough players are in the list, if not cancel the timer
                if (HideAndSeekGameGroups.Companion.listUntilX.size < minPlayers && timerRunning) {
                    for (p in HideAndSeekGameGroups.Companion.listUntilX) {
                        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f)
                    }
                    timerRunning = false
                    countdownStarted = false
                    val timer = rawTimer
                    return
                }
                // If the countdown has not started and enough players are in the list, start the countdown
                if (!countdownStarted && currentSize >= minPlayers) {
                    countdownStarted = true
                    timerRunning = true
                    val timer = rawTimer
                }

                // This is the countdown logic, if the countdown has started, send the action bar message
                if (countdownStarted) {
                    val showTimer = formatTimeUtils.formatTimer(timer)
                    for (p in HideAndSeekGameGroups.Companion.listUntilX) {
                        p.sendActionBar(
                            mm.deserialize(
                                "<gold>Spiel startet in: <color:#00E5E5>" + showTimer +
                                        "</color> (<color:#00E5E5>" + currentSize + "</color>/<color:#00E5E5>" + maxPlayers + "</color>)</gold>"
                            )
                        )
                        if (timer <= 5) {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                        }
                    }
                    if (timer <= 0 || currentSize >= maxPlayers) {
                        for (p in HideAndSeekGameGroups.Companion.listUntilX) {
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f)
                        }
                        HideAndSeekGame.startGame(plugin, config)
                        cancel()
                        waitingTask = null
                        timerRunning = false
                    }
                    timer--
                } else {
                    for (p in HideAndSeekGameGroups.Companion.listUntilX) {
                        p.sendActionBar(mm.deserialize("<gold>Warten auf weitere Spieler... (<color:#00E5E5>" + currentSize + "</color>/<color:#00E5E5>" + maxPlayers + "</color>)</gold>"))
                    }
                }
            }
        }
        waitingTask!!.runTaskTimer(plugin, 0L, 20L)
    }
}
