package de.mikeyllp.miniGamesV4.messages

import de.mikeyllp.miniGamesV4.plugin
import java.io.File

object CreateAndCheckLanguages {

    fun saveDefaultLanguagesFiles() {
        val languagesFolder = File(plugin!!.getDataFolder(), "languages")
        if (!languagesFolder.exists()) {
            languagesFolder.mkdirs()
        }
        // Creates the default locale files if they do not exist
        saveResourceIfNotExists("languages/de_de.properties")
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