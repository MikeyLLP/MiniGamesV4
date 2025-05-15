package de.mikeyllp.miniGamesV4.commands.subCommands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

import static de.mikeyllp.miniGamesV4.map.InvitetPlayerHashMap.canGameStart;


public class AcceptMiniGameCommand extends CommandAPICommand {
    public AcceptMiniGameCommand(String commandName) {
        super(commandName);

        //This is a list that adds online players to the TabCompleter and you can´t use @
        withArguments(
                new StringArgument("player")
                        .replaceSuggestions(ArgumentSuggestions.stringCollection(info ->
                                Bukkit.getOnlinePlayers().stream()
                                        .map(Player::getName)
                                        .collect(Collectors.toList())
                        ))
        );
        executesPlayer((invited, args) -> {
            Player inviter = Bukkit.getPlayerExact(args.get(0).toString());
            String prefix = "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR>";
            if (inviter == null) {
                invited.sendRichMessage(prefix + "<red>Der Spieler ist nicht online.</red>");
                return;
            }
            //Checks if the player has permission to use this command
            if (!invited.hasPermission("minigamesv4.minigames")) {
                invited.sendRichMessage(prefix + "<red>Du hast keine Berechtigung, um diesen Command zu nutzen.</red>");
                return;
            }

            //Checks if the player can play
            canGameStart(inviter, invited);

        });
    }
}
