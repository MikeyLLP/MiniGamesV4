package de.mikeyllp.miniGamesV4.utils

import de.mikeyllp.miniGamesV4.plugin
import dev.slne.surf.surfapi.core.api.util.object2ObjectMapOf
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.gradle.internal.impldep.org.apache.commons.compress.harmony.pack200.PackingUtils.config
import kotlin.io.path.div

object MessageUtils {

    lateinit var prefix: Component

    var activeLanguage: String = "en_US"
        private set
    private val langConfigs = object2ObjectMapOf<String, YamlConfiguration>()

    fun getActiveLangConfig() = getLangConfig(activeLanguage)

    fun initCustomTags() {
        TagResolver.resolver("prefix") { queue, context ->
            Tag.inserting(prefix)
        }
    }

    private fun getLangConfig(lang: String): YamlConfiguration {
        if (langConfigs.containsKey(lang)) {
            return langConfigs[lang]!!
        }

        val config = YamlConfiguration.loadConfiguration((plugin.dataPath / "langauges" / "$lang.yml").toFile())
        langConfigs[lang] = config

        return config
    }

    fun reloadConfig() {
        plugin.reloadConfig()
        langConfigs.clear()

        activeLanguage = plugin.config.getString("language", "en_US") ?: "en_US"
        prefix = MiniMessage.miniMessage().deserialize(plugin.config.getString("prefix", "ERROR")!!)
    }

    // You can use this very easily because you only set the message and the prefix are automatically added.
    fun sendMessage(sender: CommandSender, languageKey: String) {
        val langKeyValue = getLangConfig(activeLanguage).getString(languageKey)
            ?: error("Language key '$languageKey' not found in '$activeLanguage' language file.")

        sender.sendMessage(MiniMessage.miniMessage().deserialize(langKeyValue))
    }

    // You can use this to send a help message with the command and description.
    fun sendHelpMessage(sender: CommandSender, command: String, description: String) {
        sender.sendRichMessage("<white>$command </white> <gray>$description")
    }


    // Normal messages
    // Info
    fun sendNeedReloadMessage(sender: CommandSender) {
        sendMessage(
            sender,
            getLangConfig(config()?.getString("language")).getString("normal-message.info.reload")
        )
    }

    fun sendGameSwitch(sender: CommandSender, game: String, state: Boolean) {
        if (state) {
            sendMessage(
                sender, getLangConfig(config()?.getString("language"))
                    .getString("normal-message.info.enabled-game")!!.replace("%game%", game)
            )
            return
        }
        sendMessage(
            sender, getLangConfig(config()?.getString("language"))
                .getString("normal-message.info.disabled-game")!!.replace("%game%", game)
        )
    }

    fun needHelpMessage(sender: CommandSender) {
        sendMessage(
            sender,
            getLangConfig(config()?.getString("language")).getString("normal-message.info.need-help")!!
                .replace("%command%", config()?.getString("command")!!)
        )
    }

    // Error Messages
    fun sendNoPermissionMessage(sender: CommandSender) {
        sendCustomWarnMessage(
            sender,
            getLangConfig(config()?.getString("language")).getString("warning-message.no-permission")
        )
    }

    fun miniGamesDisabledMessage(sender: CommandSender) {
        sendCustomWarnMessage(
            sender,
            getLangConfig(config()?.getString("language")).getString("warning-message.disabled-game")
        )
    }

}
