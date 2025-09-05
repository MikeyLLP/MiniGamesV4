package de.mikeyllp.miniGamesV4.commands

import de.mikeyllp.miniGamesV4.messages.Translator
import de.mikeyllp.miniGamesV4.permission.MinigamesPermissionRegistry
import de.mikeyllp.miniGamesV4.plugin
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand

fun CommandAPICommand.helpCommand() = subcommand("help") {
    withPermission(MinigamesPermissionRegistry.COMMAND_HELP)

    val command = plugin.config.getString("command") ?: "minigames"

    anyExecutor { sender, args ->
        sender.sendMessage(Translator.translatable("help.header"))
        sender.sendMessage(Translator.translatable("help.info"))
        sender.sendMessage(Translator.translatable("help.sections.general.title"))
        sender.sendMessage(Translator.translatable("help.sections.general.commands.help", command))
        sender.sendMessage(Translator.translatable("help.sections.general.commands.play", command))
        sender.sendMessage(Translator.translatable("help.sections.general.commands.accept", command))
        sender.sendMessage(Translator.translatable("help.sections.general.commands.decline", command))
        sender.sendMessage(Translator.translatable("help.sections.general.commands.quit", command))
        sender.sendMessage(Translator.translatable("help.sections.general.commands.toggle", command))
        sender.sendMessage(Translator.translatable("help.sections.games.title"))
        sender.sendMessage(Translator.translatable("help.sections.games.list"))
        if (sender.hasPermission(MinigamesPermissionRegistry.COMMAND_ADMIN_HELP)) {
            sender.sendMessage(Translator.translatable("help.sections.admin.title"))
            sender.sendMessage(Translator.translatable("help.sections.admin.commands.reload", command))
            sender.sendMessage(Translator.translatable("help.sections.admin.commands.clear", command))
            sender.sendMessage(Translator.translatable("help.sections.admin.commands.set", command))
        }
        sender.sendRichMessage("help.footer")
    }
}
