package de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static de.mikeyllp.miniGamesV4.games.hideandseek.utils.formatTimeUtils.formatTimerWithText;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.*;

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
                "HASHints",
                "small-slot")));

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

            String lang = plugin.getConfig().getString("language");
            File file = new File(plugin.getDataFolder(), "languages/" + lang + ".yml");
            YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(file);


            // all settings for who has an int value
            switch (msg) {
                case "minhasplayers":
                    sendCustomMessage(sender, langConfig.getString("normal-message.info.min-players-HAS")
                            .replace("%number%", String.valueOf(someInt)));
                    config.set("minPlayersPerHASGroup", someInt);
                    plugin.saveConfig();
                    break;
                case "maxhasplayers":
                    sendCustomMessage(sender, langConfig.getString("normal-message.info.max-players-HAS")
                            .replace("%number%", String.valueOf(someInt)));
                    config.set("maxPlayersPerHASGroup", someInt);
                    plugin.saveConfig();
                    break;
                case "maxhasseekers":
                    sendCustomMessage(sender, langConfig.getString("normal-message.info.max-seekers")
                            .replace("%number%", String.valueOf(someInt)));
                    config.set("maxSeekersPerHASGroup", someInt);
                    plugin.saveConfig();
                    break;
                case "timehasautostart":
                    sendCustomMessage(sender, langConfig.getString("normal-message.info.time-autostart-HAS")
                            .replace("%number%", formatTimerWithText(someInt)));
                    config.set("timeAutoStartHASGroup", someInt);
                    plugin.saveConfig();
                    break;
                case "hasplaytime":
                    sendCustomMessage(sender, langConfig.getString("normal-message.info.play-time-HAS")
                            .replace("%number%", formatTimerWithText(someInt)));
                    config.set("playTimeHAS", someInt);
                    plugin.saveConfig();
                    break;
                case "hashidetime":
                    sendCustomMessage(sender, langConfig.getString("normal-message.info.hide-time-HAS")
                            .replace("%number%", formatTimerWithText(someInt)));
                    config.set("hideTimeHAS", someInt);
                    plugin.saveConfig();
                    break;
                case "hashints":
                    sendCustomMessage(sender, langConfig.getString("normal-message.info.hints-HAS")
                            .replace("%number%", String.valueOf(someInt)));
                    config.set("HASHints", someInt);
                    plugin.saveConfig();
                    break;
                case "small-slot":
                    if (!(someInt >= 1 && someInt <= 9)) {
                        sendCustomWarnMessage(sender, langConfig.getString("warning-message.invalid-number"));
                        return;
                    }
                    sendCustomMessage(sender, langConfig.getString("normal-message.info.small-slot")
                            .replace("%number%", String.valueOf(someInt)));
                    config.set("small-modus.slot", someInt);
                    plugin.saveConfig();
                    break;
                default:
                    sendCustomWarnMessage(sender, langConfig.getString("warning-message.invalid-config-use"));
                    break;
            }
            sendNeedReloadMessage(sender);
        }));

    }
}
