package de.mikeyllp.miniGamesV4.games.hideandseek.storage

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import java.io.File
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.ceil

class HideAndSeekGameGroups {
    // Tread-safe counter
    private val counter = AtomicInteger(0)

    // Give the time in Milliseconds back
    private val startTime = System.currentTimeMillis()

    // Method to generate a unique group name
    private fun generateGroupName(): String {
        val count = counter.getAndIncrement()
        return "Group - " + count + " - " + startTime
    }

    companion object {
        val listUntilX: MutableList<Player> = ArrayList<Player>()
        val seekerList: MutableList<Player> = ArrayList<Player>()
        val noMoveList: MutableList<Player> = ArrayList<Player>()
        val noMoveGroup: MutableMap<String?, MutableList<Player?>?> = HashMap<String?, MutableList<Player?>?>()
        val gameGroup: MutableMap<String?, MutableList<Player?>?> = HashMap<String?, MutableList<Player?>?>()
        val seekerGroup: MutableMap<String?, MutableList<Player?>?> = HashMap<String?, MutableList<Player?>?>()

        val gameState: MutableMap<String?, HideAndSeekState?> = HashMap<String?, HideAndSeekState?>()

        // To remove the Nametag
        val scoreboard: Scoreboard = Bukkit.getScoreboardManager().getMainScoreboard()
        var hiddenNameTag: Team? = scoreboard.getTeam("hideNameTags")

        fun createGroupFromHAS(playerCount: Int, plugin: JavaPlugin) {
            val gen = HideAndSeekGameGroups()
            val groupName = gen.generateGroupName()
            val playerCopy: MutableList<Player> = ArrayList<Player>(listUntilX)
            val config = plugin.getConfig()

            gameGroup.put(groupName, playerCopy)

            // Checks if a group is already existing
            if (hiddenNameTag == null) {
                hiddenNameTag = scoreboard.registerNewTeam("hideNameTags")
                hiddenNameTag!!.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
            }


            // This Loop hides all players who are not in the group
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

                hiddenNameTag!!.addEntry(viewer.getName())
            }


            var i = 0
            // When there are more players as wish, set the max seekers
            var targetSeekers: Int = calculateSeekers(playerCount)
            if ((config.getInt("maxSeekersPerHASGroup")) != 0 && targetSeekers < config.getInt("maxSeekersPerHASGroup.value")) {
                targetSeekers = config.getInt("maxSeekersPerHASGroup.value")
            }

            val lang = plugin.getConfig().getString("language")
            val file = File(plugin.getDataFolder(), "languages/" + lang + ".yml")
            val langConfig = YamlConfiguration.loadConfiguration(file)

            val mm = MiniMessage.miniMessage()
            val seekerMessage = mm.deserialize(langConfig.getString("special-message.you-seeker")!!)
            // Randomly select a seeker from the group
            while (i < targetSeekers) {
                val randomNumber = (Math.random() * playerCopy.size).toInt()
                val seeker = playerCopy.get(randomNumber)
                if (!seekerList.contains(seeker)) {
                    seekerList.add(seeker)
                    noMoveList.add(seeker)

                    val posSeeker = seeker.getLocation()
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

            // Send the hider a message that he is a Hider. And Sends all player the player list
            val hiderMessage = mm.deserialize(langConfig.getString("special-message.you-hider")!!)

            // To set a item if is enabled
            val item = ItemStack(Material.PUFFERFISH)
            val slot = config.getInt("small-modus.slot") - 1

            for (p in listUntilX) {
                p.sendRichMessage(langConfig.getString("special-message.seeker")!!)
                for (p2 in seekerList) {
                    p.sendRichMessage("<gold>" + p2.getName())
                    if (config.getBoolean("small-modus.is-enabled")) {
                        p2.getInventory().setItem(slot, item)
                    }
                }
                p.sendRichMessage("")
                p.sendRichMessage(langConfig.getString("special-message.hider")!!)
                for (p3 in listUntilX) {
                    if (!seekerList.contains(p3)) {
                        val posHider = p3.getLocation()
                        p3.playSound(posHider, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
                        p3.showTitle(
                            Title.title(
                                hiderMessage,
                                Component.text(""),
                                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
                            )
                        )
                        p.sendRichMessage("<gold>" + p3.getName())
                        // Makes the hiders smaller
                        if (config.getBoolean("small-modus.is-enabled")) {
                            p3.getAttribute(Attribute.SCALE)!!.setBaseValue(0.5)
                        }
                    }
                }
            }
            // Add the List of the Seekers to the seekerGroup
            val SeekerCopy: MutableList<Player?> = ArrayList<Player?>(seekerList)
            seekerGroup.put(groupName, SeekerCopy)

            // Add the seekers to the noMoveList
            val noMoveCopy: MutableList<Player?> = ArrayList<Player?>(noMoveList)
            noMoveGroup.put(groupName, noMoveCopy)


            // Remove seekers from the player list
            val state = HideAndSeekState(groupName, plugin, config.getInt("playTimeHAS"))
            state.hideTime()
            gameState.put(groupName, state)


            listUntilX.clear()
            noMoveList.clear()
            seekerList.clear()
        }

        // calculate the number of seekers based on the number of players
        fun calculateSeekers(players: Int): Int {
            if (players < 20) {
                return 1
            }
            return ceil(players / 20.0).toInt() * 2
        }
    }
}
