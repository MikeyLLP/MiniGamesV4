package de.mikeyllp.miniGamesV4.games.hideandseek.storage

import de.mikeyllp.miniGamesV4.games.hideandseek.utils.FormatTimeUtils
import de.mikeyllp.miniGamesV4.games.hideandseek.utils.RemovePlayersHideAndSeek
import de.mikeyllp.miniGamesV4.messages.MessageUtils
import de.mikeyllp.miniGamesV4.messages.Translator
import de.mikeyllp.miniGamesV4.plugin
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.roundToInt

class HideAndSeekState(
    private val groupName: String,
    private val searchTime: Int
) {
    private val groupList: MutableList<Player>
    private val seekerList: MutableList<Player>
    private val noMoveList: MutableList<Player>

    private val hideTime = 0

    private var hideTask: BukkitRunnable? = null
    private var inGameTask: BukkitRunnable? = null

    init {
        this.groupList = HideAndSeekGameGroups.gameGroup.getOrDefault(groupName, ArrayList<Player>())
        this.seekerList = HideAndSeekGameGroups.seekerGroup.getOrDefault(groupName, ArrayList<Player>())
        this.noMoveList = HideAndSeekGameGroups.noMoveGroup.getOrDefault(groupName, ArrayList<Player>())
    }

    fun hideTime() {
        val config = plugin.config
        val hideTimerFirst = config.getInt("hideTimeHAS")


        for (p in groupList) {
            p.allowFlight = false
            p.foodLevel = 20
            p.health = 20.0
        }

        hideTask = object : BukkitRunnable() {
            var hideTimeLeft: Int = hideTimerFirst

            override fun run() {
                val hideTimer = FormatTimeUtils.formatTimer(hideTimeLeft)

                for (p in groupList) {
                    p.sendActionBar(Translator.translatable("has.action-bar.game-start", hideTimer))
                    if (hideTimeLeft <= 5) {
                        if (hideTimeLeft == 0) {
                            MessageUtils.sendMessage(
                                p,
                                Translator.translatable("has.message.game-start.now")
                            )
                        } else {
                            p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                            MessageUtils.sendMessage(
                                p,
                                Translator.translatable("has.message.game-start.under5", hideTimeLeft.toString())
                            )
                        }
                    }
                }

                for (p in seekerList) {
                    p.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 40, 100, false, false, false))
                }

                if (hideTimeLeft <= 0) {
                    for (p in HideAndSeekGameGroups.noMoveGroup.getOrDefault(groupName, ArrayList())) {
                        p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f)
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

        val timerFirst = config.getInt("playTimeHAS")
        val hints = config.getInt("HASHints")
        val hintsTime = config.getInt("HintTimeHAS")

        val hintTimes: MutableList<Int?> = ArrayList<Int?>()
        val hintRemove: MutableList<Int?> = ArrayList<Int?>()

        val intervallSek = (timerFirst / hints).toLong()
        val hintTime = intervallSek.toFloat().roundToInt()

        for (i in 0..hints) {
            val someHint = hintTime * i
            hintTimes.add(someHint)
            hintRemove.add(someHint + hintsTime)
        }

        inGameTask = object : BukkitRunnable() {
            var timeLeft: Int = timerFirst


            override fun run() {
                val hidersSize = groupList.size - seekerList.size
                val timer = FormatTimeUtils.formatTimer(timeLeft)

                if (hintTimes.contains(timeLeft)) {
                    for (p in groupList) {
                        p.isGlowing = true
                    }
                }

                if (hintRemove.contains(timeLeft)) {
                    for (p in groupList) {
                        p.isGlowing = false
                    }
                }

                for (p in groupList) {
                    p.sendActionBar(
                        Translator.translatable(
                            "has.action-bar.hide-time.players-left",
                            timer,
                            hidersSize.toString()
                        )
                    )
                    if (timeLeft == 60) {
                        p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)

                        MessageUtils.sendMessage(
                            p,
                            Translator.translatable("has.message.hide-time.under60")
                        )
                    }
                    if (timeLeft == 30) {
                        p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)

                        MessageUtils.sendMessage(
                            p,
                            Translator.translatable("has.message.hide-time.under30")
                        )
                    }
                    if (timeLeft == 15) {
                        p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)

                        MessageUtils.sendMessage(
                            p,
                            Translator.translatable("has.message.hide-time.under15")
                        )
                    }
                    if (timeLeft <= 5) {
                        if (timeLeft == 0) {
                            MessageUtils.sendMessage(
                                p,
                                Translator.translatable("has.message.hide-time.end.now")
                            )
                        } else {
                            p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)

                            MessageUtils.sendMessage(
                                p,
                                Translator.translatable("has.message.hide-time.end.under5", timeLeft.toString())
                            )
                        }
                    }
                }

                if (timeLeft <= 0 || seekerList.isEmpty()) {
                    for (p in groupList) {

                        if (seekerList.contains(p)) {
                            val seekerLoc = p.location

                            p.playSound(seekerLoc, Sound.ENTITY_VILLAGER_DEATH, 1.0f, 1.0f)

                            MessageUtils.sendMessage(
                                p,
                                Translator.translatable("has.message.hider-win")
                            )
                            continue
                        }

                        val hiderLoc = p.location

                        p.playSound(hiderLoc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)

                        MessageUtils.sendMessage(
                            p,
                            Translator.translatable("has.message.hider-win")
                        )
                    }
                    for (p in ArrayList<Player>(groupList)) {
                        RemovePlayersHideAndSeek.playerRemove(p, "gameEnd")
                    }
                    cancel()
                    return
                }

                if (seekerList.size >= groupList.size) {
                    for (p in seekerList) {
                        p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)

                        MessageUtils.sendMessage(
                            p,
                            Translator.translatable("has.message.seeker-win")
                        )
                    }
                    for (p in ArrayList<Player>(groupList)) {
                        RemovePlayersHideAndSeek.playerRemove(p, "gameEnd")
                    }
                    HideAndSeekGameGroups.Companion.gameState.remove(groupName)
                    cancel()
                    return
                }
                timeLeft--
            }
        }
        inGameTask?.runTaskTimer(plugin, 0L, 20L)
    }

    fun stopAllTasks() {
        if (hideTask != null) {
            hideTask?.cancel()
            hideTask = null
        }
        if (inGameTask != null) {
            inGameTask?.cancel()
            inGameTask = null
        }
    }
}