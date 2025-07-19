package de.mikeyllp.miniGamesV4.utils;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.reloadConfig;

public class CreateAndCheckLanguages {
    // To get the Config
    private static JavaPlugin plugin;

    public static void init(JavaPlugin pl) {
        plugin = pl;
        reloadConfig();
    }


    public static void saveDefaultLanguagesFiles() {
        File languagesFolder = new File(plugin.getDataFolder(), "languages");
        if (!languagesFolder.exists()) {
            languagesFolder.mkdirs();
        }
        // Creates the default locale files if they do not exist
        saveResourceIfNotExists("languages/de_de.yml");
        saveResourceIfNotExists("languages/en_us.yml");
    }

    // The method to create the language files
    private static void saveResourceIfNotExists(String resourcePath) {
        File file = new File(plugin.getDataFolder(), resourcePath);
        if (!file.exists()) {
            plugin.saveResource(resourcePath, false);
        }
    }
}
