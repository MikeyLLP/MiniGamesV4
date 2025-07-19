package de.mikeyllp.miniGamesV4.commands

import de.mikeyllp.miniGamesV4.plugin
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import de.mikeyllp.miniGamesV4.utils.MinigamesPermissionRegistry
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand

fun CommandAPICommand.helpCommand() = subcommand("help") {
    withPermission(MinigamesPermissionRegistry.COMMAND_HELP)
    anyExecutor { sender, args ->

        val langConfig = MessageUtils.getActiveLangConfig()

        val cmdReplace = plugin.config.getString("command") ?: "ERROR"
        //default commands
        sender.sendRichMessage("<gold>========== [<gradient:#00FF00:#007F00>MiniGames Help</gradient>] ==========</gold>")
        sender.sendRichMessage("<color:#00E5E5><> = Pflicht | [] = Optional</color>")
        sender.sendMessage("")
        sender.sendRichMessage("<color:#00FFD5>Allgemeine Befehle:")
        // General Commands
        val generalCommands = langConfig.getConfigurationSection("special-message.help.sections.general.commands")
        if (generalCommands != null) {
            for (key in generalCommands.getKeys(false)) {
                val cmd = generalCommands.getStringList(key)
                val command = cmd[0]?.replace("%command%", cmdReplace)
                MessageUtils.sendHelpMessage(sender, command!!, cmd[1])
            }
        }
        sender.sendMessage("")
        sender.sendRichMessage("<color:#00FFD5>Spiele:")
        sender.sendMessage("")

        val games = langConfig.getStringList("special-message.help.sections.games.list")

        for (rawGame in games) {
            val game = rawGame.replace("%command%", cmdReplace)
            sender.sendRichMessage(game)
        }

        // Here are the Admin Commands
        if (sender.hasPermission(MinigamesPermissionRegistry.COMMAND_ADMIN_HELP)) {
            sender.sendMessage("")
            sender.sendRichMessage("<color:#00FFD5>Admin Befehle:")

            val adminCommands = langConfig.getConfigurationSection("special-message.help.sections.admin.commands")
            if (adminCommands != null) {
                for (key in adminCommands.getKeys(false)) {
                    val cmd = adminCommands.getStringList(key)
                    val command = cmd[0]!!.replace("%command%", cmdReplace)
                    MessageUtils.sendHelpMessage(sender, command, cmd[1])
                }
            }
        }
        sender.sendRichMessage("<gold>====================================</gold>")

    }
}
