package de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import static de.mikeyllp.miniGamesV4.commands.admincommands.ReloadConfigCommand.reloadConfig;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class SetEnableDisableGame extends CommandAPICommand {
    public SetEnableDisableGame(String commandName, JavaPlugin plugin) {

        super(commandName);


        withArguments(new StringArgument("miniGames").replaceSuggestions(ArgumentSuggestions.strings("HideAndSeek", "TicTacToe", "RPS")));


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

                        // Reload the config to apply changes
                        reloadConfig(sender, plugin);
                    } else {
                        config.set("HideAndSeek", true);

                        // Save the config
                        plugin.saveConfig();

                        // Reload the config to apply changes
                        reloadConfig(sender, plugin);
                    }
                    break;
                case "rps":
                    if (config.getBoolean("RockPaperScissors")) {
                        config.set("RockPaperScissors", false);

                        // Save the config
                        plugin.saveConfig();

                        // Reload the config to apply changes
                        reloadConfig(sender, plugin);
                    } else {
                        config.set("RockPaperScissors", true);

                        // Save the config
                        plugin.saveConfig();

                        // Reload the config to apply changes
                        reloadConfig(sender, plugin);
                    }
                    break;
                case "tictactoe":
                    if (config.getBoolean("TicTacToe")) {
                        config.set("TicTacToe", false);

                        // Save the config
                        plugin.saveConfig();

                        // Reload the config to apply changes
                        reloadConfig(sender, plugin);
                    } else {
                        config.set("TicTacToe", true);

                        // Save the config
                        plugin.saveConfig();

                        // Reload the config to apply changes
                        reloadConfig(sender, plugin);
                    }
                    break;
                default:
                    sendCustomMessage(sender, "<red>Invalid miniGame name:" + " <gold>HideAndSeek" + " TicTacToe" + " RPS");
            }
        }));
    }
}
