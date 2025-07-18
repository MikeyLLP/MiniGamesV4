package de.mikeyllp.miniGamesV4.commands;

import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.time.Duration;

import static de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups.listUntilX;
import static de.mikeyllp.miniGamesV4.games.hideandseek.utils.removePlayersHideAndSeek.playerRemove;
import static de.mikeyllp.miniGamesV4.games.rps.RPSGame.playerGameState;
import static de.mikeyllp.miniGamesV4.games.rps.RPSGame.removePlayersFromList;
import static de.mikeyllp.miniGamesV4.storage.ClickInviteStorage.enableListener;
import static de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.gameInfo;
import static de.mikeyllp.miniGamesV4.utils.ClickInviteUtils.removePlayer;
import static de.mikeyllp.miniGamesV4.utils.MessageUtils.*;

public class QuitCommand extends CommandAPICommand {


    public QuitCommand(String commandName, JavaPlugin plugin) {
        super(commandName);
        executesPlayer(((sender, args) -> {
            // Checks if the player has permission to use this command
            if (!sender.hasPermission("minigamesv4.minigames")) {
                sendNoPermissionMessage(sender);
                return;
            }

            String lang = plugin.getConfig().getString("language");
            File file = new File(plugin.getDataFolder(), "languages/" + lang + ".yml");
            YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(file);

            if (enableListener.containsKey(sender)) {
                MiniMessage mm = MiniMessage.miniMessage();
                Component miniGameComponent = mm.deserialize(langConfig.getString("special-message.click-invite-disable"));
                Component message = mm.deserialize("");
                sender.showTitle(Title.title(miniGameComponent,
                        message, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))));
                removePlayer(sender);
                return;
            }

            // Check if a game is existing
            if (gameInfo.isEmpty()) {
                sendCustomWarnMessage(sender, langConfig.getString("warning-message.nothing-to-quit"));
                return;
            }

            // Checks if the player is in a game
            if (playerGameState.containsKey(sender)) {
                Player opponent = gameInfo.get(sender);
                sendCustomWarnMessage(sender, langConfig.getString("warning-message.game-quit"));
                sendCustomWarnMessage(opponent, langConfig.getString("warning-message.player-quit").replace("%player%", sender.getName()));

                //removes the inviter and invited from the maps
                removePlayersFromList(sender, opponent);
                return;
            }


            if (playerRemove(sender, "quit", plugin)) {
                gameInfo.remove(sender);
                // Remove the player from the queue
            } else {
                if (!listUntilX.contains(sender)) {
                    sendCustomWarnMessage(sender, langConfig.getString("warning-message.nothing-to-quit"));
                    return;
                }
                gameInfo.remove(sender);
                listUntilX.removeIf(value -> value.equals(sender));
                sendCustomWarnMessage(sender, langConfig.getString("warning-message.queue-quit"));
            }
        }));
    }
}
