package de.mikeyllp.miniGamesV4.utils

import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object CreateAndCheckLanguages {
    // To get the Config
    private var plugin: JavaPlugin? = null

    fun init(pl: JavaPlugin) {
        plugin = pl
        MessageUtils.reloadConfig()
    }


    fun saveDefaultLanguagesFiles() {
        val languagesFolder = File(plugin!!.getDataFolder(), "languages")
        if (!languagesFolder.exists()) {
            languagesFolder.mkdirs()
        }
        // Creates the default locale files if they do not exist
        saveResourceIfNotExists("languages/de_de.yml")
        saveResourceIfNotExists("languages/en_us.yml")
    }

    // The method to create the language files
    private fun saveResourceIfNotExists(resourcePath: String) {
        val file = File(plugin!!.getDataFolder(), resourcePath)
        if (!file.exists()) {
            plugin!!.saveResource(resourcePath, false)
        }
    }
}
