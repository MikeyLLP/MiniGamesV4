package de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands

import de.mikeyllp.miniGamesV4.messages.MessageUtils
import de.mikeyllp.miniGamesV4.messages.Translator
import de.mikeyllp.miniGamesV4.plugin
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import java.util.*

fun CommandAPICommand.setEnableDisableGame() = subcommand("invert") {
    withArguments(
        StringArgument("miniGames").replaceSuggestions(
            ArgumentSuggestions.strings(
                "HideAndSeek", "TicTacToe", "RockPaperScissors", "SmallModus"
            )
        )
    )

    anyExecutor { sender, args ->
        val miniGameArg = args["miniGames"] as String
        val msg: String = miniGameArg.trim { it <= ' ' }.lowercase(Locale.getDefault())
        val config = plugin.config

        when (msg) {
            "hideandseek" -> if (config.getBoolean("HideAndSeek")) {
                config.set("HideAndSeek", false)

                plugin.saveConfig()

                MessageUtils.sendGameSwitch(sender, "HideAndSeek", false)
                MessageUtils.sendNeedReloadMessage(sender)
            } else {
                config.set("HideAndSeek", true)

                plugin.saveConfig()

                MessageUtils.sendGameSwitch(sender, "HideAndSeek", true)
                MessageUtils.sendNeedReloadMessage(sender)
            }

            "rockpaperscissors" -> if (config.getBoolean("RockPaperScissors")) {
                config.set("RockPaperScissors", false)

                plugin.saveConfig()

                MessageUtils.sendGameSwitch(sender, "RockPaperScissors", false)
                MessageUtils.sendNeedReloadMessage(sender)
            } else {
                config.set("RockPaperScissors", true)

                plugin.saveConfig()

                MessageUtils.sendGameSwitch(sender, "RockPaperScissors", true)
                MessageUtils.sendNeedReloadMessage(sender)
            }

            "tictactoe" -> if (config.getBoolean("TicTacToe")) {
                config.set("TicTacToe", false)

                plugin.saveConfig()

                MessageUtils.sendGameSwitch(sender, "TicTacToe", false)
                MessageUtils.sendNeedReloadMessage(sender)
            } else {
                config.set("TicTacToe", true)

                plugin.saveConfig()

                MessageUtils.sendGameSwitch(sender, "TicTacToe", true)
                MessageUtils.sendNeedReloadMessage(sender)
            }

            "smallmodus" -> if (config.getBoolean("small-modus")) {
                config.set("small-modus", false)

                plugin.saveConfig()

                MessageUtils.sendGameSwitch(sender, "small-modus", false)
                MessageUtils.sendNeedReloadMessage(sender)
            } else {
                config.set("small-modus", true)

                plugin.saveConfig()

                MessageUtils.sendGameSwitch(sender, "small-modus", true)
                MessageUtils.sendNeedReloadMessage(sender)
            }

            else -> {
                MessageUtils.sendMessage(sender, Translator.translatable("warning-message.false-game"))
            }
        }
    }
}
