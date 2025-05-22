package de.mikeyllp.miniGamesV4.commands;

import de.mikeyllp.miniGamesV4.commands.invites.EnableClickInviteCommand;
import de.mikeyllp.miniGamesV4.commands.invites.InvitesHideAndSeekGameCommand;
import de.mikeyllp.miniGamesV4.commands.invites.InvitesRPSGameCommand;
import de.mikeyllp.miniGamesV4.commands.invites.InvitesTicTacToeGameCommand;
import de.mikeyllp.miniGamesV4.gui.MenuMain;
import dev.jorel.commandapi.CommandAPICommand;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;


public class MainCommand extends CommandAPICommand {

    public MainCommand(String commandName) {
        super(commandName);

        // Here we add the SubCommands
        withSubcommand(new EnableClickInviteCommand("ClickInvite"));
        withSubcommand(new InvitesHideAndSeekGameCommand("HideAndSeek"));
        withSubcommand(new InvitesRPSGameCommand("RPS"));
        withSubcommand(new InvitesTicTacToeGameCommand("TicTacToe"));
        withSubcommand(new AcceptCommand("accept"));
        withSubcommand(new DeclinedCommand("declined"));
        withSubcommand(new ToggleInvitesCommand("toggle"));
        withSubcommand(new HelpCommand("help"));
        // If no subcommand is given, it opens the MiniGamesMenu
        executesPlayer((sender, args) -> {
            if (!sender.hasPermission("minigamesv4.minigames")) {
                sendNoPermissionMessage(sender);
                return;
            }
            MenuMain.openGameMenue(sender);
        });
    }
}
