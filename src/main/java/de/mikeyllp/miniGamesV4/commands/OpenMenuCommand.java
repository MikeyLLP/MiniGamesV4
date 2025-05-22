package de.mikeyllp.miniGamesV4.commands;

import de.mikeyllp.miniGamesV4.commands.invites.InvitesRPSGameCommand;
import de.mikeyllp.miniGamesV4.commands.invites.InvitesTicTacToeGameCommand;
import de.mikeyllp.miniGamesV4.gui.MenuMain;
import dev.jorel.commandapi.CommandAPICommand;



public class OpenMenuCommand extends CommandAPICommand {

    public OpenMenuCommand(String commandName) {
        super(commandName);

        //Here we add the SubCommands
        withSubcommand(new InvitesTicTacToeGameCommand("TicTacToe"));
        withSubcommand(new InvitesRPSGameCommand("RPS"));
        withSubcommand(new AcceptCommand("accept"));
        withSubcommand(new DeclinedCommand("declined"));
        withSubcommand(new ToggleInvitesCommand("toggle"));
        withSubcommand(new HelpCommand("help"));
        //If no subcommand is given, it opens the MiniGamesMenue
        executesPlayer((sender, args) -> {
            if(!sender.hasPermission("minigamesv4.minigames")){
                String prefix = "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR>";
                sender.sendRichMessage(prefix + "<red>Du hast keine Berechtigung, um diesen Command zu nutzen.</red>");
                return;
            }
            MenuMain.openGameMenue(sender);
        });
    }
}
