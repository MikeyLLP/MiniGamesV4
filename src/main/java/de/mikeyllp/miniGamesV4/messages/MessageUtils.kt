package de.mikeyllp.miniGamesV4.messages

import de.mikeyllp.miniGamesV4.config
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.command.CommandSender

object MessageUtils {

    lateinit var prefix: Component


    fun initCustomTags() {
        TagResolver.resolver("prefix") { queue, context ->
            Tag.inserting(prefix)
        }
    }


    fun sendMessage(sender: CommandSender, message: Component) {
        sender.sendMessage(message)
    }

    fun sendNeedReloadMessage(sender: CommandSender) {
        sendMessage(
            sender,
            Translator.translatable("normal-message.info.reload")
        )
    }

    fun sendGameSwitch(sender: CommandSender, game: String, state: Boolean) {
        val key = if (state) "enabled-game" else "disabled-game"

        sendMessage(
            sender, Translator.translatable("normal-message.info.$key", game)
        )
    }

    fun needHelpMessage(sender: CommandSender) {
        val command = config.getString("command") ?: "minigames"
        sendMessage(
            sender,
            Translator.translatable("normal-message.info.need-help", command)
        )
    }


    fun sendNoPermissionMessage(sender: CommandSender) {
        sendMessage(
            sender,
            Translator.translatable("warning-message.no-permission")
        )
    }

    fun miniGamesDisabledMessage(sender: CommandSender) {
        sendMessage(
            sender,
            Translator.translatable("warning-message.disabled-game")
        )
    }

}