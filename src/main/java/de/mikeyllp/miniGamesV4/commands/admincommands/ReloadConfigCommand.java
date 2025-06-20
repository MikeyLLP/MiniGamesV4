package de.mikeyllp.miniGamesV4.commands.admincommands;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;
import static de.mikeyllp.miniGamesV4.utils.clearUtils.clearAllLists;

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
        clearAllLists(sender, plugin);
    }
}
