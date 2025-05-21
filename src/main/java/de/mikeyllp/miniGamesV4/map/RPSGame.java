package de.mikeyllp.miniGamesV4.map;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static de.mikeyllp.miniGamesV4.map.InvitetPlayerHashMap.gameInfo;
import static de.mikeyllp.miniGamesV4.methods.GameUtil.checkGameResultForPlayers;

public class RPSGame implements Listener {
    //This map is used to check if a player is playing this game, in order to activate the ChatListener.
    private static final Map<Player, Player> inGameStatus = new HashMap<>();
    private static final Map<Player, GameState> playerGameState = new HashMap<>();


    public static class GameState {
        Player inviter;
        Player invited;
        //to check if both players made their move
        int waitingForPlayer = 0;
        //points for both players
        int pointsInviter = 0;
        int pointsInvited = 0;
        //String for the fight
        String fightItemInviter = "";
        String fightItemInvited = "";
    }

    public static void startRPSGame(Player inviter, Player invited) {

        //game State
        GameState state = new GameState();

        //both players get added to the game
        playerGameState.put(inviter, state);
        playerGameState.put(invited, state);

        inGameStatus.put(inviter, invited);

        state.inviter = inviter;
        state.invited = invited;


        String prefix = "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR>";

        MiniMessage mm = MiniMessage.miniMessage();

        Component rspGameComponent = mm.deserialize("<color:#00E5E5>Erhalte 3 Punkte. Wähle im Chat aus!</color>");
        Component choseMsg = mm.deserialize(prefix + "<bold><gray><insert:Schere><hover:show_text:'Shift + Klick zum Auswählen'>[Schere]</hover></insert>" +
                " <dark_gray><insert:Stein><hover:show_text:'Shift + Klick zum Auswählen'>[Stein]</hover></insert>" +
                " <white><insert:Papier><hover:show_text:'Shift + Klick zum Auswählen'>[Papier]</hover></insert></bold>" +
                " <red><bold><insert:cancel><hover:show_text:'Shift + Klick zum Auswählen'>gib auf</hover></insert></bold></red>");

        inviter.showTitle(Title.title(Component.text("§6Spiel Startet!"), rspGameComponent, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))));
        invited.showTitle(Title.title(Component.text("§6Spiel Startet!"), rspGameComponent, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))));

        inviter.sendMessage(choseMsg);
        invited.sendMessage(choseMsg);


    }

    //This method is used to check which message the player sends in the chat to play the game
    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {

        //game State
        GameState state = playerGameState.get(event.getPlayer());

        if (state == null) return;

        String prefix = "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR><color:#00E5E5>";

        //get the message from the event and convert it to lower case so that the player can write it in any case
        String msg = PlainTextComponentSerializer.plainText().serialize(event.message()).trim().toLowerCase();


        MiniMessage mm = MiniMessage.miniMessage();
        Component failMsg = mm.deserialize(prefix + "<red>Bitte wähle <bold><gray><insert:Schere><hover:show_text:'Shift + Klick zum Auswählen'>[Schere]</hover></insert></gray></bold>," +
                " <bold><dark_gray><insert:Stein><hover:show_text:'Shift + Klick zum Auswählen'>[Stein]</hover></insert></dark_gray></bold>," +
                " <white><bold><insert:Papier><hover:show_text:'Shift + Klick zum Auswählen'>[Papier]</hover></insert></bold></white>" +
                " oder <bold><insert:cancel><hover:show_text:'Shift + Klick zum Auswählen'>gib auf</hover></insert></bold>.</red>");


        if (inGameStatus.containsKey(event.getPlayer())) {
            event.setCancelled(true);
            Player inviter = state.inviter;
            Player invited = state.invited;

            //checks if the player made already his move
            if (!(msg.equals("cancel"))) {
                if (!(state.fightItemInviter.isEmpty())) {
                    inviter.sendRichMessage(prefix + "<red>Du hast bereits deine Wahl getroffen!</red>");
                    return;
                }
            }
            //checks which message send the player
            switch (msg) {
                case "schere":
                    state.fightItemInviter = "Schere";
                    state.waitingForPlayer++;
                    break;
                case "stein":
                    state.fightItemInviter = "Stein";
                    state.waitingForPlayer++;
                    break;
                case "papier":
                    state.fightItemInviter = "Papier";
                    state.waitingForPlayer++;
                    break;
                case "cancel":
                    inviter.sendRichMessage(prefix + "<red>Du hast das Spiel abgebrochen!</red>");
                    invited.sendRichMessage(prefix + "<gold>" + inviter.getName() + "</gold><red> hat das Spiel aufgegeben!</red>");

                    //removes the inviter and invited from the maps
                    removePlayersFromList(inviter, invited);
                    return;
                default:
                    inviter.sendMessage(failMsg);
                    return;
            }
            //checks if both players have made their move
            if (state.waitingForPlayer == 2) {
                checkWin(inviter, invited, state);
                return;
            }
            inviter.sendRichMessage(prefix + "Warten auf anderen Spieler...</color>");
        }

        if (inGameStatus.containsValue(event.getPlayer())) {

            Player inviter = state.inviter;
            Player invited = state.invited;
            event.setCancelled(true);

            //checks if the player made already his move
            if (!(msg.equals("cancel"))) {
                if (!(state.fightItemInvited.isEmpty())) {
                    invited.sendRichMessage(prefix + "<red>Du hast bereits deine Wahl getroffen!</red>");
                    return;
                }
            }

            //checks which message send the player
            switch (msg) {
                case "schere":
                    state.fightItemInvited = "Schere";
                    state.waitingForPlayer++;
                    break;
                case "stein":
                    state.fightItemInvited = "Stein";
                    state.waitingForPlayer++;
                    break;
                case "papier":
                    state.fightItemInvited = "Papier";
                    state.waitingForPlayer++;
                    break;
                case "cancel":
                    invited.sendRichMessage(prefix + "<red>Du hast das Spiel abgebrochen!</red>");
                    inviter.sendRichMessage(prefix + "<gold>" + invited.getName() + " </gold><red>hat das Spiel aufgegeben!</red>");

                    //removes the inviter and invited from the maps
                    removePlayersFromList(inviter, invited);
                    return;
                default:
                    invited.sendMessage(failMsg);
                    return;
            }

            //checks if both players have made their move
            if (state.waitingForPlayer == 2) {
                checkWin(inviter, invited, state);
                return;
            }
            invited.sendRichMessage(prefix + "Warten auf anderen Spieler...</color>");
        }
    }

    private static void checkWin(Player inviter, Player invited, GameState state) {
        MiniMessage mm = MiniMessage.miniMessage();

        Component fight = mm.deserialize("<gold>" + state.fightItemInviter + " vs " + state.fightItemInvited + "</gold>");
        Component draw = mm.deserialize("<color:#00E5E5>Unentschieden!</color>");


        //case if it´s a draw
        if (state.fightItemInviter.equals(state.fightItemInvited)) {
            inviter.showTitle(Title.title(fight, draw, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))));
            invited.showTitle(Title.title(fight, draw, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))));

            checkGameResultForPlayers(inviter, invited, 2, true, false);

            //reset the states
            state.fightItemInviter = "";
            state.fightItemInvited = "";
            state.waitingForPlayer = 0;
            return;
        }

        //checks if the inviter wins that player who wins get A point up
        if (state.fightItemInviter.equals("Stein") && state.fightItemInvited.equals("Schere") ||
                state.fightItemInviter.equals("Schere") && state.fightItemInvited.equals("Papier") ||
                state.fightItemInviter.equals("Papier") && state.fightItemInvited.equals("Stein")) {

            state.pointsInviter++;

            String points = updatePoints(state.pointsInviter, state.pointsInvited);
            String pointsB = updatePoints(state.pointsInvited, state.pointsInviter);

            Component win = mm.deserialize("<color:#00E5E5> Du hast gewonnen! <gold>+1 Punkt</gold></color> " + points);
            Component loseB = mm.deserialize("<color:#00E5E5> Du hast verloren!</color> " + pointsB);


            inviter.showTitle(Title.title(fight, win, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))));
            invited.showTitle(Title.title(fight, loseB, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))));


            checkGameResultForPlayers(inviter, invited, 1, true, false);
        } else {
            state.pointsInvited++;

            String points = updatePoints(state.pointsInviter, state.pointsInvited);
            String pointsB = updatePoints(state.pointsInvited, state.pointsInviter);

            Component winB = mm.deserialize("<color:#00E5E5> Du hast gewonnen! <gold>+1 Punkt</gold></color> " + pointsB);
            Component lose = mm.deserialize("<color:#00E5E5> Du hast verloren!</color> " + points);

            invited.showTitle(Title.title(fight, winB, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))));
            inviter.showTitle(Title.title(fight, lose, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))));
            checkGameResultForPlayers(invited, inviter, 1, true, false);
        }
        //reset the states
        state.fightItemInviter = "";
        state.fightItemInvited = "";
        state.waitingForPlayer = 0;

        //checks if one of the players has 3 points
        if (state.pointsInviter == 3) {
            checkGameResultForPlayers(inviter, invited, 1, false, true);
            removePlayersFromList(inviter, invited);
        } else if (state.pointsInvited == 3) {
            checkGameResultForPlayers(invited, inviter, 1, false, true);
            removePlayersFromList(inviter, invited);
        }
    }

    //removes the player from the game when he leaves the server
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!inGameStatus.containsKey(event.getPlayer())) return;


        Player player = event.getPlayer();
        String prefix = "<COLOR:DARK_GRAY>>> </COLOR><gradient:#00FF00:#007F00>MiniGames </gradient><COLOR:DARK_GRAY>| </COLOR>";
        inGameStatus.get(player).sendRichMessage(prefix + "<red>" + player.getName() + " hat das Spiel verlassen!</red>");

        removePlayersFromList(player, inGameStatus.get(player));
    }

    //updates the points of the players
    private static String updatePoints(int you, int opponent) {
        return "<color:#00E5E5>Du: <gold>" + you + "</gold> | <gold>Gegner: " + opponent + "</color>";
    }

    //removes the players form the list
    private static void removePlayersFromList(Player inviter, Player invited) {

        gameInfo.remove(inviter);
        gameInfo.remove(invited);

        playerGameState.remove(inviter);
        playerGameState.remove(invited);

        inGameStatus.remove(inviter, invited);

    }


}
