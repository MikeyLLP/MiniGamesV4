package de.mikeyllp.miniGamesV4.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.time.Duration;

import static de.mikeyllp.miniGamesV4.storage.ClickInviteStorage.*;

public class ClickInviteUtils {

    // Shows the Title to the Player and add the Listener
    public static void enableClickInvite(Player player, String miniGame) {
        addEnableListener(player, miniGame);
        MiniMessage mm = MiniMessage.miniMessage();
        Component miniGameComponent = mm.deserialize("<gold>Click Invite<gold> <green>Enabled</green>");
        Component message = mm.deserialize("<color:#00E5E5>Spieler anklicken.</color:#00E5E5> <red>'cancel' zum Abbrechen.</red>");
        player.closeInventory();
        player.showTitle(Title.title(miniGameComponent,
                message, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))));
    }

    // Sends the player an invite if they right- or left-click on another player.
    public static void playerGotClicked(Player inviter, Player invited) {
        inviter.performCommand("minigames " + whatGame.get(inviter) + " " + invited.getName());
        removePlayer(inviter);
    }

    // Removes the Player from the enable Listener Map
    public static void removePlayer(Player inviter) {
        enableListener.remove(inviter);
        whatGame.remove(inviter);
    }
}
