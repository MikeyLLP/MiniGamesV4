package de.mikeyllp.miniGamesV4.commands.admincommands.setsubcommands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.WorldArgument;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import static de.mikeyllp.miniGamesV4.commands.admincommands.ReloadConfigCommand.reloadConfig;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;

public class SetHASSpawn extends CommandAPICommand {
    public SetHASSpawn(String commandName, JavaPlugin plugin) {
        super(commandName);

        withArguments(new LocationArgument("location"));
        withArguments(new WorldArgument("world"));
        executes((sender, args) -> {
            // Checks if the player has permission to use this command
            if (!sender.hasPermission("minigamesv4.admin")) {
                sendNoPermissionMessage(sender);
                return;
            }
            // Get the Config
            FileConfiguration config = plugin.getConfig();

            World worldArg = (World) args.get("world");
            Location locationArg = (Location) args.get("location");


            double locX = locationArg.getX();
            double locY = locationArg.getY();
            double locZ = locationArg.getZ();

            // Set the spawn location in the config
            config.set("spawn-location.world", worldArg.getName());
            config.set("spawn-location.x", locX);
            config.set("spawn-location.y", locY);
            config.set("spawn-location.z", locZ);

            // Save the config
            plugin.saveConfig();

            // Reload the config to apply changes
            reloadConfig(sender, plugin);
        });
    }
}

