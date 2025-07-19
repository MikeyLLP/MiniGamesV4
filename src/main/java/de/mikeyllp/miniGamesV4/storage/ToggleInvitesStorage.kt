package de.mikeyllp.miniGamesV4.storage;

import de.mikeyllp.miniGamesV4.MiniGamesV4;
import de.mikeyllp.miniGamesV4.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.sendCustomMessage;

public class ToggleInvitesStorage {

    private final Database db;

    public ToggleInvitesStorage(Database db) {
        this.db = db;
    }

    public void checkToggle(Player player, Consumer<Boolean> callback) {
        UUID playerUuid = player.getUniqueId();
        db.getToggleAsync(playerUuid).thenAccept(toggle -> {
            // This is a short way to check if its 0 or 1
            boolean result = toggle == 0;
            Bukkit.getScheduler().runTask(MiniGamesV4.getInstance(), () -> {
                callback.accept(result);
            });
        });
    }


    //Adds or removes the player from the toggle list
    public void addToggle(Player player) {

        UUID playerUuid = player.getUniqueId();
        db.getToggleAsync(playerUuid).thenAccept(toggle -> {
            // This is if or else in a single line
            int newToggle = (toggle == 1) ? 0 : 1;

            db.setToggleAsync(playerUuid, newToggle).thenRun(() -> {
                Bukkit.getScheduler().runTask(
                        MiniGamesV4.getInstance(),
                        () -> {
                            if (newToggle == 1) {
                                sendCustomMessage(player, "<color:#00E5E5>Du kannst jetzt nicht mehr eingeladen werden!</color:#00E5E5>");
                            } else {
                                sendCustomMessage(player, "<color:#00E5E5>Du kannst jetzt wieder eingeladen werden!</color:#00E5E5>");
                            }
                        });
            });
        });
    }
}
