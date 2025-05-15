package de.mikeyllp.miniGamesV4.commands.subCommands;

import dev.jorel.commandapi.CommandAPICommand;

import static de.mikeyllp.miniGamesV4.map.ToggleInvitesHashMap.addToggle;

public class ToggleInvitesCommand extends CommandAPICommand {
    public ToggleInvitesCommand(String commandName) {
        super(commandName);
        //A Command witch toggle that he can get Invited
        executesPlayer(((sender, commandArguments) -> {
            //Checks if the player has permission to use this command
            if(!sender.hasPermission("minigamesv4.minigames")){
                String prefix = "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR>";
                sender.sendRichMessage(prefix + "<red>Du hast keine Berechtigung, um diesen Command zu nutzen.</red>");
                return;
            }
            addToggle(sender.getPlayer());
        }));
    }
}
