package de.mikeyllp.miniGamesV4.commands.admincommands;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import static de.mikeyllp.miniGamesV4.game.hideandseek.HideAndSeekGame.timerList;
import static de.mikeyllp.miniGamesV4.utils.CheckConfig.checkAndFixingConfig;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.*;

public class ReloadConfigCommand extends CommandAPICommand {

    public ReloadConfigCommand(String commandName, JavaPlugin plugin) {
        super(commandName);

        executes((sender, args) -> {
            // Checks if the player has permission to use this command
            if (!sender.hasPermission("minigamesv4.admin")) {
                sendNoPermissionMessage(sender);
                return;
            }
            reloadConfig(sender, plugin);
        });
    }

    public static void reloadConfig(CommandSender sender, JavaPlugin plugin) {
        // Check to Reload the config
        try {
            plugin.reloadConfig();
            timerList.clear();

            // Check if the config is current cerated
            if (checkAndFixingConfig(plugin)) {
                sendCustomMessage(sender, "Config reloaded successfully!");
                checkAndFixingConfig(plugin);
                return;
            }
            sendCustomMessage(sender, "<red> \u26A0 Invalid config.yml detected! The config will be reset to default values and backed up. \u26A0</red>");

        } catch (Exception e) {
            sender.sendRichMessage(prefix() + "<red> An error occurred while reloading the config: " + e.getMessage());
        }
    }
}
