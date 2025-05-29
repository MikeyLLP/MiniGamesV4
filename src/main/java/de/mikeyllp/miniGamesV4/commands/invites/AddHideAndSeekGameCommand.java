package de.mikeyllp.miniGamesV4.commands.invites;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.java.JavaPlugin;

import static de.mikeyllp.miniGamesV4.game.hideandseek.HideAndSeekGame.addPlayerToHAS;
import static de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.gameInfo;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.*;

public class AddHideAndSeekGameCommand extends CommandAPICommand {
    public AddHideAndSeekGameCommand(String commandName, JavaPlugin plugin) {
        super(commandName);
        // This command will add the player to a game list.
        executesPlayer((sender, args) -> {
            // Checks if the player has permission to use this command.
            if (!sender.hasPermission("minigamesv4.minigames")) {
                sendNoPermissionMessage(sender);
                return;
            }

            // Checks if the Hide And Seek is enabled in the config.
            boolean isEnabled = plugin.getConfig().getBoolean("HideAndSeek");
            if (!isEnabled) {
                miniGamesDisabledMessage(sender);
                return;
            }

            if (gameInfo.containsKey(sender)) {
                sendAlreadyInGameMessage(sender);
                return;
            }

            addPlayerToHAS(sender, plugin);
        });
    }
}
