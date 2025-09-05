package de.mikeyllp.miniGamesV4

import de.mikeyllp.miniGamesV4.commands.mainCommand
import de.mikeyllp.miniGamesV4.database.Database
import de.mikeyllp.miniGamesV4.games.hideandseek.listeners.HideAndSeekListeners
import de.mikeyllp.miniGamesV4.games.hideandseek.listeners.NoSeekerMove
import de.mikeyllp.miniGamesV4.games.hideandseek.listeners.PlayerHoldItemListener
import de.mikeyllp.miniGamesV4.games.rps.RPSGame
import de.mikeyllp.miniGamesV4.games.tictactoe.TicTacToeGame
import de.mikeyllp.miniGamesV4.listeners.PlayerJoinQuitListener
import de.mikeyllp.miniGamesV4.messages.CreateAndCheckLanguages
import de.mikeyllp.miniGamesV4.messages.MessageUtils
import de.mikeyllp.miniGamesV4.storage.ClickInviteStorage
import de.mikeyllp.miniGamesV4.storage.ToggleInvitesStorage
import de.mikeyllp.miniGamesV4.utils.CheckConfigUtils
import net.kyori.adventure.text.logger.slf4j.ComponentLogger.logger
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.max


val plugin get() = JavaPlugin.getPlugin(MiniGamesV4::class.java)
val storage get() = plugin.toggleInvitesStorage
val config get() = plugin.config


class MiniGamesV4 : JavaPlugin() {
    lateinit var db: Database
        private set

    lateinit var toggleInvitesStorage: ToggleInvitesStorage
        private set


    override fun onEnable() {
        logger().info("MiniGamesV4 is starting...")

        saveDefaultConfig()
        CheckConfigUtils.checkAndFixingConfig()

        CreateAndCheckLanguages.saveDefaultLanguagesFiles()

        MessageUtils.initCustomTags()


        db = Database()
        db.connect()
        db.createTable()

        this.toggleInvitesStorage = ToggleInvitesStorage(db)

        val manager = server.pluginManager

        manager.registerEvents(ClickInviteStorage(), this)
        manager.registerEvents(HideAndSeekListeners(this), this)
        manager.registerEvents(PlayerJoinQuitListener(this), this)
        manager.registerEvents(RPSGame(), this)
        manager.registerEvents(TicTacToeGame, this)
        manager.registerEvents(NoSeekerMove(), this)
        manager.registerEvents(PlayerHoldItemListener(this), this)

        mainCommand()

        val green = "§a"
        val console = Bukkit.getConsoleSender()
        val version = description.version


        val lineLength = 74
        val padding = (lineLength - version.length) / 2
        val versionLine = " ".repeat(max(0, padding)) + "v$version"

        console.sendMessage("$green    __  ___ _         _  ______                              _    __ __ __")
        console.sendMessage("$green   /  |/  /(_)____   (_)/ ____/____ _ ____ ___   ___   _____| |  / // // /")
        console.sendMessage("$green  / /|_/ // // __ \\ / // / __ / __ `// __ `__ \\ / _ \\ / ___/| | / // // /_")
        console.sendMessage("$green / /  / // // / / // // /_/ // /_/ // / / / / //  __/(__  ) | |/ //__  __/")
        console.sendMessage("$green/_/  /_//_//_/ /_//_/ \\____/ \\__,_//_/ /_/ /_/ \\___//____/  |___/   /_/   ")
        console.sendMessage("§6$versionLine")
    }

    override fun onDisable() {

        db.disconnect()

        logger().info("Bye <3")
    }
}
