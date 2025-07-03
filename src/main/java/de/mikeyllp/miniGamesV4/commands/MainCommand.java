package de.mikeyllp.miniGamesV4.commands;

import de.mikeyllp.miniGamesV4.MiniGamesV4;
import de.mikeyllp.miniGamesV4.commands.admincommands.ReloadConfigCommand;
import de.mikeyllp.miniGamesV4.commands.admincommands.SetCommand;
import de.mikeyllp.miniGamesV4.commands.admincommands.clearCommand;
import de.mikeyllp.miniGamesV4.commands.invites.AddHideAndSeekGameCommand;
import de.mikeyllp.miniGamesV4.commands.invites.InvitesRPSGameCommand;
import de.mikeyllp.miniGamesV4.commands.invites.InvitesTicTacToeGameCommand;
import de.mikeyllp.miniGamesV4.database.Database;
import de.mikeyllp.miniGamesV4.gui.MenuMain;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.java.JavaPlugin;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendNoPermissionMessage;


public class MainCommand extends CommandAPICommand {

    public MainCommand(String commandName, JavaPlugin plugin, Database db) {
        super(commandName);

        var storage = ((MiniGamesV4) plugin).getToggleInvitesStorage();

        // Here we add the SubCommands
        // Admin Commands
        withSubcommand(new clearCommand("clear", plugin));
        withSubcommand(new ReloadConfigCommand("reload", plugin));
        withSubcommand(new SetCommand("set", plugin));

        // Normal Commands
        withSubcommand(new AddHideAndSeekGameCommand("HideAndSeek", plugin));
        withSubcommand(new InvitesRPSGameCommand("RPS", plugin, storage));
        withSubcommand(new InvitesTicTacToeGameCommand("TicTacToe", plugin, storage));
        withSubcommand(new AcceptCommand("accept"));
        withSubcommand(new DeclinedCommand("declined"));
        withSubcommand(new ToggleInvitesCommand("toggle", storage));
        withSubcommand(new QuitCommand("quit", plugin));
        withSubcommand(new HelpCommand("help", plugin));
        // If no subcommand is given, it opens the MiniGamesMenu
        executesPlayer((sender, args) -> {
            if (!sender.hasPermission("minigamesv4.minigames")) {
                sendNoPermissionMessage(sender);
                return;
            }
            MenuMain.openGameMenue(sender, plugin);
        });
    }
}
