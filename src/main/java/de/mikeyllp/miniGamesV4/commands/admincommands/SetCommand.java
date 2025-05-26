package de.mikeyllp.miniGamesV4.commands.admincommands;

import de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands.SetEnableDisableGame;
import de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands.SetHASSpawn;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.java.JavaPlugin;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;

public class SetCommand extends CommandAPICommand {
    public SetCommand(String commandName, JavaPlugin plugin) {
        super(commandName);
        withSubcommand(new SetHASSpawn("HASSpawn", plugin));
        withSubcommand(new SetEnableDisableGame("invert", plugin));

        executes(((sender, args) -> {
            if (sender.hasPermission("minigamesv4.admin")) {
                sendNoPermissionMessage(sender);
            }
            sendCustomMessage(sender, "Need Help? Use /minigames help");
        }));
    }
}
