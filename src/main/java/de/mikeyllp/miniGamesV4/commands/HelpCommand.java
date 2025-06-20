package de.mikeyllp.miniGamesV4.commands;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendHelpMessage;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;

public class HelpCommand extends CommandAPICommand {
    public HelpCommand(String commandName, JavaPlugin plugin) {
        super(commandName);
        // Only an executor for the console
        executes(((sender, args) -> {
            //Checks if the player has permission to use this command
            if (!sender.hasPermission("minigamesv4.minigames")) {
                sendNoPermissionMessage(sender);
                return;
            }
            String lang = plugin.getConfig().getString("language");
            File file = new File(plugin.getDataFolder(), "languages/" + lang + ".yml");
            YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(file);

            String cmdReplace = plugin.getConfig().getString("command");

            //default commands
            sender.sendRichMessage("<gold>========== [<gradient:#00FF00:#007F00>MiniGames Help</gradient>] ==========</gold>");
            sender.sendRichMessage("<color:#00E5E5><> = Pflicht | [] = Optional</color>");
            sender.sendMessage("");
            sender.sendRichMessage("<color:#00FFD5>Allgemeine Befehle:");
            // General Commands
            ConfigurationSection generalCmds = langConfig.getConfigurationSection("special-message.help.sections.general.commands");
            for (String key : generalCmds.getKeys(false)) {
                List<String> cmd = generalCmds.getStringList(key);
                String command = cmd.get(0).replace("%command%", cmdReplace);
                sendHelpMessage(sender, command, cmd.get(1));
            }
            sender.sendMessage("");
            sender.sendRichMessage("<color:#00FFD5>Spiele:");
            sender.sendMessage("");
            List<String> games = langConfig.getStringList("special-message.help.sections.games.list");
            for (String rawGame : games) {
                String game = rawGame.replace("%command%", cmdReplace);
                sender.sendRichMessage(game);
            }

            // Here are the Admin Commands
            if (sender.isOp() || sender.hasPermission("MiniGamesV4.admin")) {
                sender.sendMessage("");
                sender.sendRichMessage("<color:#00FFD5>Admin Befehle:");

                ConfigurationSection adminCmds = langConfig.getConfigurationSection("special-message.help.sections.admin.commands");
                for (String key : adminCmds.getKeys(false)) {
                    List<String> cmd = adminCmds.getStringList(key);
                    String command = cmd.get(0).replace("%command%", cmdReplace);
                    sendHelpMessage(sender, command, cmd.get(1));
                }
            }
            sender.sendRichMessage("<gold>====================================</gold>");
        }));
    }
}
