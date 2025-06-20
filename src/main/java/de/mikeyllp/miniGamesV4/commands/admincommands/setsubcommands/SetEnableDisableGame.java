package de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.*;

public class SetEnableDisableGame extends CommandAPICommand {
    public SetEnableDisableGame(String commandName, JavaPlugin plugin) {

        super(commandName);


        withArguments(new StringArgument("miniGames").replaceSuggestions(ArgumentSuggestions.strings(
                "HideAndSeek", "TicTacToe", "RockPaperScissors", "SmallModus")));


        executes(((sender, arg) -> {
            String miniGameArg = (String) arg.get("miniGames");
            String msg = miniGameArg.trim().toLowerCase();

            // Get the Config
            FileConfiguration config = plugin.getConfig();

            switch (msg) {
                case "hideandseek":
                    if (config.getBoolean("HideAndSeek")) {
                        config.set("HideAndSeek", false);

                        // Save the config
                        plugin.saveConfig();

                        sendGameSwitch(sender, "HideAndSeek", false);
                        sendNeedReloadMessage(sender);
                    } else {
                        config.set("HideAndSeek", true);

                        // Save the config
                        plugin.saveConfig();

                        sendGameSwitch(sender, "HideAndSeek", true);
                        sendNeedReloadMessage(sender);
                    }
                    break;
                case "rockpaperscissors":
                    if (config.getBoolean("RockPaperScissors")) {
                        config.set("RockPaperScissors", false);

                        // Save the config
                        plugin.saveConfig();

                        sendGameSwitch(sender, "RockPaperScissors", false);
                        sendNeedReloadMessage(sender);
                    } else {
                        config.set("RockPaperScissors", true);

                        // Save the config
                        plugin.saveConfig();

                        sendGameSwitch(sender, "RockPaperScissors", true);
                        sendNeedReloadMessage(sender);
                    }
                    break;
                case "tictactoe":
                    if (config.getBoolean("TicTacToe")) {
                        config.set("TicTacToe", false);

                        // Save the config
                        plugin.saveConfig();

                        sendGameSwitch(sender, "TicTacToe", false);
                        sendNeedReloadMessage(sender);
                    } else {
                        config.set("TicTacToe", true);

                        // Save the config
                        plugin.saveConfig();

                        sendGameSwitch(sender, "TicTacToe", true);
                        sendNeedReloadMessage(sender);
                    }
                    break;
                case "smallmodus":
                    if (config.getBoolean("small-modus")) {
                        config.set("small-modus", false);

                        // Save the config
                        plugin.saveConfig();

                        sendGameSwitch(sender, "small-modus", false);
                        sendNeedReloadMessage(sender);
                    } else {
                        config.set("small-modus", true);

                        // Save the config
                        plugin.saveConfig();

                        sendGameSwitch(sender, "small-modus", true);
                        sendNeedReloadMessage(sender);
                    }
                    break;
                default:
                    String lang = plugin.getConfig().getString("lang");
                    File file = new File(plugin.getDataFolder(), "languages/" + lang + ".yml");
                    sendCustomMessage(sender, YamlConfiguration.loadConfiguration(file).getString("warning-message.false-game"));
            }
        }));
    }
}
