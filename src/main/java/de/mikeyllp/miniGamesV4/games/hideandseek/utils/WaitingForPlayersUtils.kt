package de.mikeyllp.miniGamesV4.games.hideandseek.utils

import de.mikeyllp.miniGamesV4.games.hideandseek.HideAndSeekGame
import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import de.mikeyllp.miniGamesV4.messages.Translator
import de.mikeyllp.miniGamesV4.plugin
import org.bukkit.Sound
import org.bukkit.scheduler.BukkitRunnable

object WaitingForPlayersUtils {
    var waitingTask: BukkitRunnable? = null

    val timerList: MutableList<Int> = ArrayList<Int>()
    private var timerRunning = false

    fun startWaitingTask() {
        val config = plugin.getConfig()
        val rawTimer = config.getInt("timeAutoStartHASGroup")
        val maxPlayers = config.getInt("maxPlayersPerHASGroup")
        val minPlayers = config.getInt("minPlayersPerHASGroup")



        waitingTask = object : BukkitRunnable() {

            var countdownStarted: Boolean = false
            var timer: Int = rawTimer

            override fun run() {
                val currentSize: Int = HideAndSeekGameGroups.listUntilX.size

                if (currentSize == 0) {
                    cancel()
                    waitingTask = null
                    timerRunning = false
                    countdownStarted = false
                    return
                }

                if (HideAndSeekGameGroups.listUntilX.size < minPlayers && timerRunning) {
                    for (p in HideAndSeekGameGroups.listUntilX) {
                        p.playSound(p.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
                    }
                    timerRunning = false
                    countdownStarted = false
                    val timer = rawTimer
                    return
                }

                if (!countdownStarted && currentSize >= minPlayers) {
                    countdownStarted = true
                    timerRunning = true
                    val timer = rawTimer
                }

                if (countdownStarted) {
                    val showTimer = FormatTimeUtils.formatTimer(timer)
                    for (p in HideAndSeekGameGroups.listUntilX) {

                        p.sendActionBar(
                            Translator.translatable(
                                "has.action-bar.waiting-for-players.timer",
                                showTimer,
                                currentSize.toString(),
                                maxPlayers.toString()
                            )
                        )

                        if (timer <= 5) {
                            p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                        }
                    }

                    if (timer <= 0 || currentSize >= maxPlayers) {
                        for (p in HideAndSeekGameGroups.Companion.listUntilX) {
                            p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f)
                        }
                        HideAndSeekGame.startGame()
                        cancel()
                        waitingTask = null
                        timerRunning = false
                    }
                    timer--
                } else {
                    for (p in HideAndSeekGameGroups.listUntilX) {
                        p.sendActionBar(
                            Translator.translatable(
                                "has.action-bar.waiting-for-players.no-timer",
                                currentSize.toString(),
                                maxPlayers.toString(),
                            )
                        )
                    }
                }
            }
        }
        waitingTask!!.runTaskTimer(plugin, 0L, 20L)
    }
}
