package de.mikeyllp.miniGamesV4.messages

import de.mikeyllp.miniGamesV4.messages.MessageUtils.prefix
import de.mikeyllp.miniGamesV4.plugin
import dev.slne.surf.surfapi.core.api.util.object2ObjectMapOf
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import kotlin.io.path.div

object Translator {

    var activeLanguage: String = "en_US"
        private set

    private val langConfigs = object2ObjectMapOf<String, YamlConfiguration>()

    fun getActiveLangConfig() = getLangConfig(activeLanguage)

    private fun getLangConfig(lang: String): YamlConfiguration? {
        if (langConfigs.containsKey(lang)) {
            return langConfigs[lang]
        }

        val config = YamlConfiguration.loadConfiguration((plugin.dataPath / "languages" / "$lang.yml").toFile())
        langConfigs[lang] = config

        return config
    }

    //TODO get data from Database
    private fun getPlayerLanguage(sender: CommandSender): String {
        return "null"
    }


    fun getMessage(languageKey: String): String {
        val lang = getActiveLangConfig() ?: error("Language not found: $activeLanguage")
        return lang.getString(languageKey)
            ?: error("Language key '$languageKey' not found in '$activeLanguage' language file.")
    }


    fun translatable(key: String, vararg args: String): Component {

        var message = getMessage(key)

        args.forEachIndexed { i, arg ->
            message = message.replace("{$i}", arg)
        }

        return MiniMessage.miniMessage().deserialize(message)
    }


    fun reloadConfig() {
        plugin.reloadConfig()
        langConfigs.clear()

        activeLanguage = plugin.config.getString("language", "en_US") ?: "en_US"
        prefix = MiniMessage.miniMessage().deserialize(plugin.config.getString("prefix", "ERROR")!!)
    }
}