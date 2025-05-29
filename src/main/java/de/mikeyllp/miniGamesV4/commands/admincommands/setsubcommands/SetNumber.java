package de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;

public class SetNumber extends CommandAPICommand {
    public SetNumber(String commandName, JavaPlugin plugin) {
        super(commandName);

        // Subcommands with ints
        withArguments(new StringArgument("settings").replaceSuggestions(ArgumentSuggestions.strings(
                "minHASPlayers",
                "maxHASPlayers",
                "maxHASSeekers",
                "timeHASAutoStart",
                "HASPlayTime",
                "HASHideTime",
                "HASHints")));

        withArguments(new IntegerArgument("someInt"));
        executes(((sender, args) -> {
            // Check if the sender has permission to use this command
            if (!sender.hasPermission("minigamesv4.admin")) {
                sendNoPermissionMessage(sender);
                return;
            }


            FileConfiguration config = plugin.getConfig();
            String settingsArgs = (String) args.get("settings");
            String msg = settingsArgs.trim().toLowerCase();

            int someInt = (int) args.get("someInt");

            // all settings for who has an int value
            switch (msg) {
                case "minhasplayers":
                    sendCustomMessage(sender, "The minimum number of players where HAS starts has been set to: <gold>" + someInt);
                    config.set("minPlayersPerHASGroup", someInt);
                    plugin.saveConfig();
                    break;
                case "maxhasplayers":
                    sendCustomMessage(sender, "The maximum number of players where HAS starts has been set to: <gold>" + someInt);
                    config.set("maxPlayersPerHASGroup", someInt);
                    plugin.saveConfig();
                    break;
                case "maxhasseekers":
                    sendCustomMessage(sender, "The maximum number of seekers in a HAS game has been set to: <gold>" + someInt);
                    config.set("maxSeekersPerHASGroup", someInt);
                    plugin.saveConfig();
                    break;
                case "timehasautostart":
                    sendCustomMessage(sender, "The time in seconds until a HAS game starts automatically has been set to: <gold>" + someInt);
                    config.set("timeAutoStartHASGroup", someInt);
                    plugin.saveConfig();
                    break;
                case "hasplaytime":
                    sendCustomMessage(sender, "The playtime in seconds of a HAS game has been set to: <gold>" + someInt);
                    config.set("playTimeHAS", someInt);
                    plugin.saveConfig();
                    break;
                case "hashidetime":
                    sendCustomMessage(sender, "The hide time in seconds of a HAS game has been set to: <gold>" + someInt);
                    config.set("hideTimeHAS", someInt);
                    plugin.saveConfig();
                    break;
                case "hashints":
                    sendCustomMessage(sender, "The number of hints in a HAS game has been set to: <gold>" + someInt);
                    config.set("HASHints", someInt);
                    plugin.saveConfig();
                    break;
                default:
                    sendCustomMessage(sender, "<red>Invalid setting name. Please use one of the following:" +
                            " <gold>minHASPlayers, maxHASPlayers, maxHASSeekers, timeHASAutoStart, HASPlayTime, HASHideTime, HASHints");
                    break;
            }
        }));

    }
}
