package de.mikeyllp.miniGamesV4.games.hideandseek.storage

import de.mikeyllp.miniGamesV4.games.hideandseek.utils.formatTimeUtils
import de.mikeyllp.miniGamesV4.games.hideandseek.utils.removePlayersHideAndSeek
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class HideAndSeekState(// Groups
    private val groupName: String?, // Plugin instance
    private val plugin: JavaPlugin, private val searchTime: Int
) {
    private val groupList: MutableList<Player>
    private val seekerList: MutableList<Player>
    private val noMoveList: MutableList<Player?>?

    // Timer
    private val hideTime = 0

    // Task
    private var hideTask: BukkitRunnable? = null
    private var inGameTask: BukkitRunnable? = null

    // State of the game
    init {
        this.groupList = HideAndSeekGameGroups.Companion.gameGroup.get(groupName)
        this.seekerList = HideAndSeekGameGroups.Companion.seekerGroup.getOrDefault(groupName, ArrayList<Player?>())
        this.noMoveList = HideAndSeekGameGroups.Companion.noMoveGroup.getOrDefault(groupName, ArrayList<Player?>())
    }

    fun hideTime() {
        val config = plugin.getConfig()
        val hideTimerFirst = config.getInt("hideTimeHAS")

        val mm = MiniMessage.miniMessage()

        // disable flight for all players in the group
        // Set the health max
        for (p in groupList) {
            p.setAllowFlight(false)
            p.setFoodLevel(20)
            p.setHealth(20.0)
        }

        hideTask = object : BukkitRunnable() {
            var hideTimeLeft: Int = hideTimerFirst

            override fun run() {
                val hideTimer = formatTimeUtils.formatTimer(hideTimeLeft)
                // Show the action bar message to all players in the group
                for (p in groupList) {
                    p.sendActionBar(mm.deserialize("<gold>Die sucher können suchen in: <color:#00E5E5>" + hideTimer + "</color>"))
                    if (hideTimeLeft <= 5) {
                        if (hideTimeLeft == 0) {
                            MessageUtils.sendMessage(p, "Spiel startet in: <gold>Jetzt!</gold>")
                        } else {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                            MessageUtils.sendMessage(p, "Spiel startet in: <gold>" + hideTimeLeft + "</gold>")
                        }
                    }
                }

                // Give the seekers a blindness effect
                for (p in seekerList) {
                    p.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 40, 100, false, false, false))
                }

                // If the timer has reached 0, start the game
                if (hideTimeLeft <= 0) {
                    for (p in HideAndSeekGameGroups.Companion.noMoveGroup.get(groupName)) {
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f)
                    }
                    HideAndSeekGameGroups.Companion.noMoveGroup.remove(groupName)
                    startGameTask()
                    cancel()
                }
                hideTimeLeft--
            }
        }
        hideTask!!.runTaskTimer(plugin, 0L, 20L)
    }

    fun startGameTask() {
        val config = plugin.getConfig()

        // hints
        val timerFirst = config.getInt("playTimeHAS")
        val hints = config.getInt("HASHints")
        val hintsTime = config.getInt("HintTimeHAS")

        val hintTimes: MutableList<Int?> = ArrayList<Int?>()
        val hintRemove: MutableList<Int?> = ArrayList<Int?>()

        val mm = MiniMessage.miniMessage()

        // To know how long the interval is between the hints
        val intervallSek = (timerFirst / hints).toLong()
        val hintTime = Math.round(intervallSek.toFloat())

        for (i in 0..hints) {
            val someHint = hintTime * i
            hintTimes.add(someHint)
            hintRemove.add(someHint + hintsTime)
        }

        inGameTask = object : BukkitRunnable() {
            var timeLeft: Int = timerFirst


            override fun run() {
                val hidersSize = groupList.size - seekerList.size
                val timer = formatTimeUtils.formatTimer(timeLeft)

                // Here we check if the timeLeft is in the hintTimes list to set the glowing effect
                if (hintTimes.contains(timeLeft)) {
                    for (p in groupList) {
                        p.setGlowing(true)
                    }
                }

                // Here we check if the timeLeft is in the hintRemove list to remove the glowing effect
                if (hintRemove.contains(timeLeft)) {
                    for (p in groupList) {
                        p.setGlowing(false)
                    }
                }

                // Show the action bar message to all players in the group
                for (p in groupList) {
                    p.sendActionBar(mm.deserialize("<gold>Das Spiel endet in: <color:#00E5E5>" + timer + "</color> Übrige Verstecker <color:#00E5E5>" + hidersSize + "</color></gold>"))
                    if (timeLeft == 60) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                        MessageUtils.sendMessage(p, "Noch <gold>60</gold> Sekunden ")
                    }
                    if (timeLeft == 30) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                        MessageUtils.sendMessage(p, "<gold>30</gold> Sekunden")
                    }
                    if (timeLeft == 15) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                        MessageUtils.sendMessage(p, "Noch <gold>15</gold> Sekunden")
                    }
                    if (timeLeft <= 5) {
                        if (timeLeft == 0) {
                            MessageUtils.sendMessage(p, "Spiel endet in: <gold>Jetzt!</gold>")
                        } else {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                            MessageUtils.sendMessage(p, "Spiel endet in: <gold>" + timeLeft + "</gold>")
                        }
                    }
                }


                // Checks if the timer has reached 0
                if (timeLeft <= 0) {
                    for (p in groupList) {
                        if (seekerList.contains(p)) {
                            val seekerLoc = p.getLocation()
                            p.playSound(seekerLoc, Sound.ENTITY_VILLAGER_DEATH, 1.0f, 1.0f)
                            MessageUtils.sendMessage(p, "Die Verstecker haben gewonnen!")
                            continue
                        }
                        val hiderLoc = p.getLocation()
                        p.playSound(hiderLoc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
                        MessageUtils.sendMessage(p, "Die Verstecker haben gewonnen!")
                    }
                    for (p in ArrayList<Player?>(groupList)) {
                        removePlayersHideAndSeek.playerRemove(p, "gameEnd", plugin)
                    }
                    cancel()
                    return
                }


                // If there are no seekers left
                if (seekerList.isEmpty()) {
                    for (p in groupList) {
                        if (seekerList.contains(p)) {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1.0f, 1.0f)
                            MessageUtils.sendMessage(p, "Die Verstecker haben gewonnen!")
                            continue
                        }
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
                        MessageUtils.sendMessage(p, "Die Verstecker haben gewonnen!")
                    }
                    for (p in ArrayList<Player?>(groupList)) {
                        removePlayersHideAndSeek.playerRemove(p, "gameEnd", plugin)
                    }
                    cancel()
                    return
                }

                // If all hiders are found
                if (seekerList.size >= groupList.size) {
                    for (p in seekerList) {
                        MessageUtils.sendMessage(p, "Die Sucher haben gewonnen!")
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
                    }
                    for (p in ArrayList<Player?>(groupList)) {
                        removePlayersHideAndSeek.playerRemove(p, "gameEnd", plugin)
                    }
                    HideAndSeekGameGroups.Companion.gameState.remove(groupName)
                    cancel()
                    return
                }
                timeLeft--
            }
        }
        inGameTask!!.runTaskTimer(plugin, 0L, 20L)
    }

    fun stopAllTasks() {
        if (hideTask != null) {
            hideTask!!.cancel()
            hideTask = null
        }
        if (inGameTask != null) {
            inGameTask!!.cancel()
            inGameTask = null
        }
    }
}