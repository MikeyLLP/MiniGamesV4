package de.mikeyllp.miniGamesV4.commands.admincommands;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.java.JavaPlugin;

import static de.mikeyllp.miniGamesV4.utils.clearUtils.clearAllLists;

public class clearCommand extends CommandAPICommand {
    public clearCommand(String commandName, JavaPlugin plugin) {
        super(commandName);
        executes(((sender, args) -> {
            if (!sender.hasPermission("minigamesv4.admin")) {
                sender.sendMessage("<red>You do not have permission to use this command.</red>");
                return;
            }
            clearAllLists(sender, plugin);
        }));
    }
}
