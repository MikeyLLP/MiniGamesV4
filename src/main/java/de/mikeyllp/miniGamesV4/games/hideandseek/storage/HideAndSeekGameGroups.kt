package de.mikeyllp.miniGamesV4.games.hideandseek.storage

import de.mikeyllp.miniGamesV4.messages.MessageUtils.sendMessage
import de.mikeyllp.miniGamesV4.messages.Translator
import de.mikeyllp.miniGamesV4.plugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.ceil

class HideAndSeekGameGroups {
    private val counter = AtomicInteger(0)
    private val startTime = System.currentTimeMillis()

    private fun generateGroupName(): String {
        val count = counter.getAndIncrement()
        return "Group-$count-$startTime"
    }

    companion object {
        val listUntilX: MutableList<Player> = ArrayList<Player>()
        val seekerList: MutableList<Player> = ArrayList<Player>()
        val noMoveList: MutableList<Player> = ArrayList<Player>()
        val noMoveGroup: MutableMap<String, MutableList<Player>> = HashMap<String, MutableList<Player>>()
        val gameGroup: MutableMap<String, MutableList<Player>> = HashMap<String, MutableList<Player>>()
        val seekerGroup: MutableMap<String, MutableList<Player>> = HashMap<String, MutableList<Player>>()

        val gameState: MutableMap<String, HideAndSeekState> = HashMap<String, HideAndSeekState>()

        val scoreboard: Scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        var hiddenNameTag: Team? = scoreboard.getTeam("hideNameTags")

        fun createGroupFromHAS(playerCount: Int) {
            val gen = HideAndSeekGameGroups()
            val groupName = gen.generateGroupName()
            val playerCopy: MutableList<Player> = ArrayList(listUntilX)
            val config = plugin.config

            gameGroup[groupName] = playerCopy

            if (hiddenNameTag == null) {
                hiddenNameTag = scoreboard.registerNewTeam("hideNameTags")
                hiddenNameTag?.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
            }

            for (outsider in Bukkit.getOnlinePlayers()) {
                if (listUntilX.contains(outsider)) continue
                for (hidden in listUntilX) {
                    outsider.hidePlayer(plugin, hidden)
                }
            }

            for (viewer in listUntilX) {

                for (target in Bukkit.getOnlinePlayers()) {
                    if (target == viewer) continue
                    viewer.hidePlayer(plugin, target)
                }

                for (gameTarget in listUntilX) {
                    if (gameTarget == viewer) continue
                    viewer.showPlayer(plugin, gameTarget)
                }

                hiddenNameTag?.addEntry(viewer.name)
            }


            var i = 0
            var targetSeekers: Int = calculateSeekers(playerCount)
            if ((config.getInt("maxSeekersPerHASGroup")) != 0 && targetSeekers < config.getInt("maxSeekersPerHASGroup.value")) {
                targetSeekers = config.getInt("maxSeekersPerHASGroup.value")
            }

            val seekerMessage = Translator.translatable(("has.message.you-seeker"))

            while (i < targetSeekers) {
                val randomNumber = (Math.random() * playerCopy.size).toInt()
                val seeker = playerCopy[randomNumber]
                if (!seekerList.contains(seeker)) {
                    seekerList.add(seeker)
                    noMoveList.add(seeker)

                    val posSeeker = seeker.location
                    seeker.playSound(posSeeker, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
                    seeker.showTitle(
                        Title.title(
                            seekerMessage,
                            Component.text(""),
                            Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
                        )
                    )
                    i++
                }
            }

            val item = ItemStack(Material.PUFFERFISH)
            val slot = config.getInt("small-modus.slot") - 1

            for (p in listUntilX) {
                sendMessage(p, Translator.translatable("has.message.seeker"))
                for (p2 in seekerList) {
                    p.sendRichMessage("<gold>" + p2.name)
                    if (config.getBoolean("small-modus.is-enabled")) {
                        p2.inventory.setItem(slot, item)
                    }
                }

                p.sendRichMessage("")
                sendMessage(p, Translator.translatable("has.message.hider"))

                for (p3 in listUntilX) {
                    if (!seekerList.contains(p3)) {
                        val posHider = p3.location
                        p3.playSound(posHider, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
                        p3.showTitle(
                            Title.title(
                                Translator.translatable(("has.message.you-hider")),
                                Component.text(""),
                                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
                            )
                        )
                        p.sendRichMessage("<gold>" + p3.name)

                        if (config.getBoolean("small-modus.is-enabled")) {
                            p3.getAttribute(Attribute.SCALE)?.baseValue = 0.5
                        }
                    }
                }
            }

            val seekerCopy: MutableList<Player> = ArrayList(seekerList)
            seekerGroup[groupName] = seekerCopy

            val noMoveCopy: MutableList<Player> = ArrayList(noMoveList)
            noMoveGroup[groupName] = noMoveCopy

            val state = HideAndSeekState(groupName, config.getInt("playTimeHAS"))
            state.hideTime()
            gameState[groupName] = state


            listUntilX.clear()
            noMoveList.clear()
            seekerList.clear()
        }

        fun calculateSeekers(players: Int): Int {
            if (players < 20) {
                return 1
            }
            return ceil(players / 20.0).toInt() * 2
        }
    }
}
