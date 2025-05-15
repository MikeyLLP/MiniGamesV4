package de.mikeyllp.miniGamesV4.commands;

import de.mikeyllp.miniGamesV4.commands.subCommands.*;
import de.mikeyllp.miniGamesV4.gui.MiniGamesMenue;
import dev.jorel.commandapi.CommandAPICommand;



public class OpenMiniGamesMenueCommand extends CommandAPICommand {

    public OpenMiniGamesMenueCommand(String commandName) {
        super(commandName);

        //Here we add the SubCommands
        withSubcommand(new InvitesTicTacToeGameCommand("TicTacToe"));
        withSubcommand(new AcceptMiniGameCommand("accept"));
        withSubcommand(new DeclinedMiniGamesCommand("declined"));
        withSubcommand(new ToggleInvitesCommand("toggle"));
        withSubcommand(new HelpCommand("help"));
        //If no subcommand is given, it opens the MiniGamesMenue
        executesPlayer((sender, args) -> {
            if(!sender.hasPermission("minigamesv4.minigames")){
                String prefix = "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR>";
                sender.sendRichMessage(prefix + "<red>Du hast keine Berechtigung, um diesen Command zu nutzen.</red>");
                return;
            }
            MiniGamesMenue.openGameMenue(sender);
        });
    }
}
