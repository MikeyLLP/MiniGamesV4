package de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands

import de.mikeyllp.miniGamesV4.utils.MessageUtils
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

class SetEnableDisableGame(commandName: String, plugin: JavaPlugin) : CommandAPICommand(commandName) {
    init {
        withArguments(
            StringArgument("miniGames").replaceSuggestions(
                ArgumentSuggestions.strings<CommandSender?>(
                    "HideAndSeek", "TicTacToe", "RockPaperScissors", "SmallModus"
                )
            )
        )


        executes((CommandExecutor { sender: CommandSender, arg: CommandArguments ->
            val miniGameArg = arg.get("miniGames") as String
            val msg: String? = miniGameArg.trim { it <= ' ' }.lowercase(Locale.getDefault())

            // Get the Config
            val config = plugin.getConfig()
            when (msg) {
                "hideandseek" -> if (config.getBoolean("HideAndSeek")) {
                    config.set("HideAndSeek", false)

                    // Save the config
                    plugin.saveConfig()

                    MessageUtils.sendGameSwitch(sender, "HideAndSeek", false)
                    MessageUtils.sendNeedReloadMessage(sender)
                } else {
                    config.set("HideAndSeek", true)

                    // Save the config
                    plugin.saveConfig()

                    MessageUtils.sendGameSwitch(sender, "HideAndSeek", true)
                    MessageUtils.sendNeedReloadMessage(sender)
                }

                "rockpaperscissors" -> if (config.getBoolean("RockPaperScissors")) {
                    config.set("RockPaperScissors", false)

                    // Save the config
                    plugin.saveConfig()

                    MessageUtils.sendGameSwitch(sender, "RockPaperScissors", false)
                    MessageUtils.sendNeedReloadMessage(sender)
                } else {
                    config.set("RockPaperScissors", true)

                    // Save the config
                    plugin.saveConfig()

                    MessageUtils.sendGameSwitch(sender, "RockPaperScissors", true)
                    MessageUtils.sendNeedReloadMessage(sender)
                }

                "tictactoe" -> if (config.getBoolean("TicTacToe")) {
                    config.set("TicTacToe", false)

                    // Save the config
                    plugin.saveConfig()

                    MessageUtils.sendGameSwitch(sender, "TicTacToe", false)
                    MessageUtils.sendNeedReloadMessage(sender)
                } else {
                    config.set("TicTacToe", true)

                    // Save the config
                    plugin.saveConfig()

                    MessageUtils.sendGameSwitch(sender, "TicTacToe", true)
                    MessageUtils.sendNeedReloadMessage(sender)
                }

                "smallmodus" -> if (config.getBoolean("small-modus")) {
                    config.set("small-modus", false)

                    // Save the config
                    plugin.saveConfig()

                    MessageUtils.sendGameSwitch(sender, "small-modus", false)
                    MessageUtils.sendNeedReloadMessage(sender)
                } else {
                    config.set("small-modus", true)

                    // Save the config
                    plugin.saveConfig()

                    MessageUtils.sendGameSwitch(sender, "small-modus", true)
                    MessageUtils.sendNeedReloadMessage(sender)
                }

                else -> {
                    val lang = plugin.getConfig().getString("lang")
                    val file = File(plugin.getDataFolder(), "languages/" + lang + ".yml")
                    MessageUtils.sendMessage(
                        sender,
                        YamlConfiguration.loadConfiguration(file).getString("warning-message.false-game")
                    )
                }
            }
        }))
    }
}
