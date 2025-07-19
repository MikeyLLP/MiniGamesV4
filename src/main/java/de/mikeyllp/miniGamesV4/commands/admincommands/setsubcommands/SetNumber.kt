package de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands

import de.mikeyllp.miniGamesV4.games.hideandseek.utils.formatTimeUtils
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

class SetNumber(commandName: String, plugin: JavaPlugin) : CommandAPICommand(commandName) {
    init {
        // Subcommands with ints
        withArguments(
            StringArgument("settings").replaceSuggestions(
                ArgumentSuggestions.strings<CommandSender>(
                    "minHASPlayers",
                    "maxHASPlayers",
                    "maxHASSeekers",
                    "timeHASAutoStart",
                    "HASPlayTime",
                    "HASHideTime",
                    "HASHints",
                    "small-slot"
                )
            )
        )

        withArguments(IntegerArgument("someInt"))
        executes((CommandExecutor { sender: CommandSender, args: CommandArguments ->
            // Check if the sender has permission to use this command
            if (!sender.hasPermission("minigamesv4.admin")) {
                MessageUtils.sendNoPermissionMessage(sender)
                return@CommandExecutor
            }


            val config = plugin.getConfig()
            val settingsArgs = args.get("settings") as String?
            val msg = settingsArgs?.trim { it <= ' ' }?.lowercase(Locale.getDefault())

            val someInt = args.get("someInt") as Int

            val lang = plugin.getConfig().getString("language")
            val file = File(plugin.dataFolder, "languages/$lang.yml")
            val langConfig = YamlConfiguration.loadConfiguration(file)


            // all settings for who has an int value
            when (msg) {
                "minhasplayers" -> {
                    MessageUtils.sendMessage(
                        sender, langConfig.getString("normal-message.info.min-players-HAS")
                            ?.replace("%number%", someInt.toString())
                    )
                    config.set("minPlayersPerHASGroup", someInt)
                    plugin.saveConfig()
                }

                "maxhasplayers" -> {
                    MessageUtils.sendMessage(
                        sender, langConfig.getString("normal-message.info.max-players-HAS")
                            ?.replace("%number%", someInt.toString())
                    )
                    config.set("maxPlayersPerHASGroup", someInt)
                    plugin.saveConfig()
                }

                "maxhasseekers" -> {
                    MessageUtils.sendMessage(
                        sender, langConfig.getString("normal-message.info.max-seekers")
                            ?.replace("%number%", someInt.toString())
                    )
                    config.set("maxSeekersPerHASGroup", someInt)
                    plugin.saveConfig()
                }

                "timehasautostart" -> {
                    MessageUtils.sendMessage(
                        sender, langConfig.getString("normal-message.info.time-autostart-HAS")
                            ?.replace("%number%", formatTimeUtils.formatTimerWithText(someInt))
                    )
                    config.set("timeAutoStartHASGroup", someInt)
                    plugin.saveConfig()
                }

                "hasplaytime" -> {
                    MessageUtils.sendMessage(
                        sender, langConfig.getString("normal-message.info.play-time-HAS")
                            ?.replace("%number%", formatTimeUtils.formatTimerWithText(someInt))
                    )
                    config.set("playTimeHAS", someInt)
                    plugin.saveConfig()
                }

                "hashidetime" -> {
                    MessageUtils.sendMessage(
                        sender, langConfig.getString("normal-message.info.hide-time-HAS")
                            ?.replace("%number%", formatTimeUtils.formatTimerWithText(someInt))
                    )
                    config.set("hideTimeHAS", someInt)
                    plugin.saveConfig()
                }

                "hashints" -> {
                    MessageUtils.sendMessage(
                        sender, langConfig.getString("normal-message.info.hints-HAS")
                            ?.replace("%number%", someInt.toString())
                    )
                    config.set("HASHints", someInt)
                    plugin.saveConfig()
                }

                "small-slot" -> {
                    if (!(someInt >= 1 && someInt <= 9)) {
                        MessageUtils.sendCustomWarnMessage(
                            sender,
                            langConfig.getString("warning-message.invalid-number")
                        )
                        return@CommandExecutor
                    }
                    MessageUtils.sendMessage(
                        sender, langConfig.getString("normal-message.info.small-slot")
                            ?.replace("%number%", someInt.toString())
                    )
                    config.set("small-modus.slot", someInt)
                    plugin.saveConfig()
                }

                else -> MessageUtils.sendCustomWarnMessage(
                    sender,
                    langConfig.getString("warning-message.invalid-config-use")
                )
            }
            MessageUtils.sendNeedReloadMessage(sender)
        }))
    }
}
