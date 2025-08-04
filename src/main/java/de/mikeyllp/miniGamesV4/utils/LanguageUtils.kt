package de.mikeyllp.miniGamesV4.utils

import de.mikeyllp.miniGamesV4.plugin
import de.mikeyllp.miniGamesV4.utils.MessageUtils.prefix
import dev.slne.surf.surfapi.core.api.util.object2ObjectMapOf
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.configuration.file.YamlConfiguration
import java.text.MessageFormat
import kotlin.io.path.div

object LanguageUtils {

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

    fun reloadConfig() {
        plugin.reloadConfig()
        langConfigs.clear()

        activeLanguage = plugin.config.getString("language", "en_US") ?: "en_US"
        prefix = MiniMessage.miniMessage().deserialize(plugin.config.getString("prefix", "ERROR")!!)
    }

    fun getMessage(languageKey: String): String {
        val lang = getActiveLangConfig()
        return lang?.getString(languageKey)
            ?: error("Language key '$languageKey' not found in '$activeLanguage' language file.")
    }
}

fun translatable(key: String, vararg args: Any?): String {
    val raw = LanguageUtils.getMessage(key)
    return MessageFormat.format(raw, *args)
}

