package de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands

import de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands.utils.NumbersToSet
import de.mikeyllp.miniGamesV4.config
import de.mikeyllp.miniGamesV4.games.hideandseek.utils.formatTimeUtils
import de.mikeyllp.miniGamesV4.permission.MinigamesPermissionRegistry
import de.mikeyllp.miniGamesV4.plugin
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import de.mikeyllp.miniGamesV4.utils.translatable
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.multiLiteralArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import java.util.*

fun CommandAPICommand.setNumberCommand() = subcommand("number") {
    withPermission(MinigamesPermissionRegistry.COMMAND_SET)

    multiLiteralArgument("settings", *NumbersToSet.entries.map { it.argsName }.toTypedArray())
    withArguments(IntegerArgument("someInt"))

    anyExecutor { sender, args ->
        val settingsArgs = args.get("settings") as String?
        val msg = settingsArgs?.trim { it <= ' ' }?.lowercase(Locale.getDefault())

        val someInt = args.get("someInt") as Int

        when (msg) {
            "minhasplayers" -> {
                MessageUtils.sendMessage(
                    sender,
                    translatable("normal-message.info.min-players-HAS", someInt.toString())
                )
                config.set("minPlayersPerHASGroup", someInt)
            }

            "maxhasplayers" -> {
                MessageUtils.sendMessage(
                    sender,
                    translatable("normal-message.info.max-players-HAS", someInt.toString())
                )
                config.set("maxPlayersPerHASGroup", someInt)
            }

            "maxhasseekers" -> {
                MessageUtils.sendMessage(
                    sender,
                    translatable("normal-message.info.max-seekers", someInt.toString())
                )
                config.set("maxSeekersPerHASGroup", someInt)
            }

            "timehasautostart" -> {
                MessageUtils.sendMessage(
                    sender,
                    translatable("normal-message.info.time-autostart-HAS", formatTimeUtils.formatTimerWithText(someInt))
                )
                config.set("timeAutoStartHASGroup", someInt)
            }

            "hasplaytime" -> {
                MessageUtils.sendMessage(
                    sender,
                    translatable("normal-message.info.play-time-HAS", formatTimeUtils.formatTimerWithText(someInt))
                )
                config.set("playTimeHAS", someInt)
            }

            "hashidetime" -> {
                MessageUtils.sendMessage(
                    sender,
                    translatable("normal-message.info.hide-time-HAS", formatTimeUtils.formatTimerWithText(someInt))
                )
                config.set("hideTimeHAS", someInt)
            }

            "hashints" -> {
                MessageUtils.sendMessage(
                    sender,
                    translatable("normal-message.info.hints-HAS", someInt.toString())
                )
                config.set("HASHints", someInt)
            }

            "small-slot" -> {
                if (!(someInt >= 1 && someInt <= 9)) {
                    MessageUtils.sendMessage(
                        sender,
                        translatable("warning-message.invalid-number")
                    )
                    return@anyExecutor
                }

                MessageUtils.sendMessage(
                    sender, translatable("normal-message.info.small-slot", someInt.toString())
                )
                config.set("small-modus.slot", someInt)
            }

            else -> MessageUtils.sendMessage(
                sender,
                translatable("warning-message.invalid-config-use")
            )
        }
        plugin.saveConfig()
        MessageUtils.sendNeedReloadMessage(sender)
    }
}