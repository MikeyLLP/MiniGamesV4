package de.mikeyllp.miniGamesV4.map;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static de.mikeyllp.miniGamesV4.map.InvitetPlayerHashMap.gameInfo;

public class RPSGame implements Listener {
    //This map is used to check if a player is playing this game, in order to activate the ChatListener.
    private static final Map<Player, Player> inGameStatus = new HashMap<>();

    /*
    public static class GameState {
        //This boolean indicates whether the game is won or not
        boolean gameWon = false;
        //points for both players
        int pointsInviter = 0;
        int pointsInvited = 0;
    }*/

    public static void startRPSGame(Player inviter, Player invited) {

        inGameStatus.put(inviter, invited);

        MiniMessage mm = MiniMessage.miniMessage();
        Component rspGameComponent = mm.deserialize("<color:#00E5E5>Wähle im Chat aus!</color>");

        inviter.showTitle(Title.title(Component.text("§6Spiel Startet!"), rspGameComponent, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))));
        invited.showTitle(Title.title(Component.text("§6Spiel Startet!"), rspGameComponent, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))));

        String prefix = "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR>";
        Component choseMsg = mm.deserialize(prefix + "<bold><gray><insert:Schere><hover:show_text:'Shift + Klick zum Auswählen'>[Schere]</hover></insert>" +
                " <dark_gray><insert:Stein><hover:show_text:'Shift + Klick zum Auswählen'>[Stein]</hover></insert>" +
                " <white><insert:Papier><hover:show_text:'Shift + Klick zum Auswählen'>[Papier]</hover></insert></bold>" +
                "<bold><insert:cancel><hover:show_text:'Shift + Klick zum Auswählen'>gib auf</hover></insert></bold>");
        inviter.sendMessage(choseMsg);
        invited.sendMessage(choseMsg);

    }

    //
    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {

        String prefix = "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR><color:#00E5E5>";

        MiniMessage mm = MiniMessage.miniMessage();
        Component failMsg = mm.deserialize(prefix + "<red>Bitte wähle <bold><gray><insert:Schere><hover:show_text:'Shift + Klick zum Auswählen'>[Schere]</hover></insert></gray></bold>," +
                " <bold><dark_gray><insert:Stein><hover:show_text:'Shift + Klick zum Auswählen'>[Stein]</hover></insert></dark_gray></bold>," +
                " <white><bold><insert:Papier><hover:show_text:'Shift + Klick zum Auswählen'>[Papier]</hover></insert></bold></white>" +
                " oder <bold><insert:cancel><hover:show_text:'Shift + Klick zum Auswählen'>gib auf</hover></insert></bold>.</red>");

        //get the message from the event and convert it to lower case so that the player can write it in any case
        String msg = PlainTextComponentSerializer.plainText().serialize(event.message()).trim().toLowerCase();

        if (inGameStatus.containsKey(event.getPlayer())) {



            Player inviter = event.getPlayer();
            //checks which message send the player
            switch (msg) {
                case "schere":
                    inviter.sendRichMessage(prefix + "Du hast Schere gewählt!");
                    break;

                case "stein":
                    inviter.sendRichMessage(prefix + "Du hast Stein gewählt!");
                    break;

                case "papier":
                    inviter.sendRichMessage(prefix + "Du hast Papier gewählt!");
                    break;

                case "cancel":
                    inviter.sendRichMessage(prefix + "Du hast das Spiel abgebrochen!");
                    inGameStatus.get(inviter).sendRichMessage(prefix + "Der Spieler hat das Spiel abgebrochen!");

                    //removes the inviter and invited from the maps
                    gameInfo.remove(inviter);
                    gameInfo.remove(inGameStatus.get(inviter));

                    inGameStatus.remove(inviter);


                    break;
                default:
                    inviter.sendMessage(failMsg);
            }
            return;
        }

        if (inGameStatus.containsValue(event.getPlayer())) {

            Player invited = event.getPlayer();

            //checks which message send the player
            switch (msg) {
                case "schere":
                    invited.sendRichMessage(prefix + "Du hast Schere gewählt!");
                    break;

                case "stein":
                    invited.sendRichMessage(prefix + "Du hast Stein gewählt!");
                    break;

                case "papier":
                    invited.sendRichMessage(prefix + "Du hast Papier gewählt!");
                    break;

                case "cancel":
                    invited.sendRichMessage(prefix + "Du hast das Spiel abgebrochen!");
                    inGameStatus.get(invited).sendRichMessage(prefix + "Der Spieler hat das Spiel abgebrochen!");

                    //removes the inviter and invited from the maps
                    gameInfo.remove(invited);
                    gameInfo.remove(inGameStatus.get(invited));

                    inGameStatus.remove(invited);
                    break;
                default:
                    invited.sendMessage(failMsg);
            }
        }
    }


}
