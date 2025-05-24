package de.mikeyllp.miniGamesV4.commands.admincommands;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.prefix;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;

public class ReloadConfigCommand extends CommandAPICommand {

    public ReloadConfigCommand(String commandName, JavaPlugin plugin) {
        super(commandName);

        executes((sender, args) -> {
            // Checks if the player has permission to use this command
            if (!sender.hasPermission("minigamesv4.admin")) {
                sendNoPermissionMessage((Player) sender);
                return;
            }
            // Check to Reload the config
            try {
                plugin.reloadConfig();
                sender.sendRichMessage(prefix() + "Config reloaded successfully!");
            } catch (Exception e) {
                sender.sendRichMessage(prefix() + "<red> An error occurred while reloading the config: " + e.getMessage());

            }


        });
    }
}
