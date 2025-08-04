package de.mikeyllp.miniGamesV4.utils

import de.mikeyllp.miniGamesV4.config
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
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


    fun sendMessage(sender: CommandSender, message: String) {

        sender.sendMessage(MiniMessage.miniMessage().deserialize(message))
    }

    fun sendNeedReloadMessage(sender: CommandSender) {
        sendMessage(
            sender,
            translatable("normal-message.info.reload")
        )
    }

    fun sendGameSwitch(sender: CommandSender, game: String, state: Boolean) {
        val key = if (state) "enabled-game" else "disabled-game"

        sendMessage(
            sender, translatable("normal-message.info.$key", game)
        )
    }

    fun needHelpMessage(sender: CommandSender) {
        val command = config.getString("command") ?: "minigames"
        sendMessage(
            sender,
            translatable("normal-message.info.need-help", command)
        )
    }


    fun sendNoPermissionMessage(sender: CommandSender) {
        sendMessage(
            sender,
            translatable("warning-message.no-permission")
        )
    }

    fun miniGamesDisabledMessage(sender: CommandSender) {
        sendMessage(
            sender,
            translatable("warning-message.disabled-game")
        )
    }

}
