package de.mikeyllp.miniGamesV4;

import de.mikeyllp.miniGamesV4.commands.MainCommand;
import de.mikeyllp.miniGamesV4.game.hideandseek.HideAndSeekListeners;
import de.mikeyllp.miniGamesV4.game.rps.RPSGame;
import de.mikeyllp.miniGamesV4.game.tictactoe.TicTacToeGame;
import de.mikeyllp.miniGamesV4.storage.ClickInviteStorage;
import de.mikeyllp.miniGamesV4.storage.ToggleInvitesStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static de.mikeyllp.miniGamesV4.utils.CheckConfig.checkAndFixingConfig;


public final class MiniGamesV4 extends JavaPlugin {


    private static MiniGamesV4 instance;

    @Override
    public void onEnable() {

        instance = this;

        //Thanks to ChatGPT for the Logo XD
        String green = "§a";
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String version = getDescription().getVersion();

        int lineLength = 74;
        int padding = (lineLength - version.length()) / 2;
        String versionLine = " ".repeat(Math.max(0, padding)) + "v" + version;

        console.sendMessage(green + "    __  ___ _         _  ______                              _    __ __ __");
        console.sendMessage(green + "   /  |/  /(_)____   (_)/ ____/____ _ ____ ___   ___   _____| |  / // // /");
        console.sendMessage(green + "  / /|_/ // // __ \\ / // / __ / __ `// __ `__ \\ / _ \\ / ___/| | / // // /_");
        console.sendMessage(green + " / /  / // // / / // // /_/ // /_/ // / / / / //  __/(__  ) | |/ //__  __/");
        console.sendMessage(green + "/_/  /_//_//_/ /_//_/ \\____/ \\__,_//_/ /_/ /_/ \\___//____/  |___/   /_/   ");
        console.sendMessage("§6" + versionLine);


        PluginManager manager = getServer().getPluginManager();

        //Register the Listener
        manager.registerEvents(new ClickInviteStorage(), this);
        manager.registerEvents(new TicTacToeGame(), this);
        manager.registerEvents(new ClickInviteStorage(), this);
        manager.registerEvents(new ToggleInvitesStorage(), this);
        manager.registerEvents(new RPSGame(), this);
        manager.registerEvents(new HideAndSeekListeners(), this);

        new MainCommand("minigames", this).register();

        // Generate the config.yml if it does not exist
        saveDefaultConfig();
        // Check if the config is current
        checkAndFixingConfig(this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Bye <3");

    }

    //This is for the Runnable
    public static MiniGamesV4 getInstance() {
        return instance;
    }
}
