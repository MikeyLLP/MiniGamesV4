package de.mikeyllp.miniGamesV4.storage;

import de.mikeyllp.miniGamesV4.MiniGamesV4;
import de.mikeyllp.miniGamesV4.game.rps.RPSGame;
import de.mikeyllp.miniGamesV4.game.tictactoe.TicTacToeGame;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static de.mikeyllp.miniGamesV4.utils.MessageUtils.*;

public class InvitePlayerStorage {


    //Handelt all Invites
    public static final Map<PlayerKey, String> invitesManager = new HashMap<>();
    //Deletes all invites after X seconds
    public static final Map<PlayerKey, BukkitTask> invitesTasks = new HashMap<>();
    //To check if the player is already in a game
    public static final Map<Player, Player> gameInfo = new HashMap<>();


    //This method adds Checks if the invited player is already in a game or not an adds the invite to the HashMap for 60 seconds
    public static void addInvite(Player inviter, Player invited, String game) {
        //Check if the Player can be invited
        if (gameInfo.containsKey(invited.getPlayer())) {
            sendCustomMessage(inviter, "<RED>Der Spieler befindet sich gerade in einem Spiel.</RED>");
            return;
        }

        PlayerKey key = new PlayerKey(String.valueOf(inviter.getPlayer()), String.valueOf(invited.getPlayer()));

        //If a new invite is created the old one will be removed
        invitesManager.remove(key);
        invitesManager.put(key, game);

        //A Runnable is created to remove the invite after 60 seconds
        invitesTasks.put(key, new BukkitRunnable() {
            @Override
            public void run() {
                removeInvite(inviter, invited);
                sendCustomMessage(inviter, "<color:#00E5E5>Die Einladung von dir an<gold> " + invited.getName() + " </gold>ist abgelaufen.</color:#00E5E5>");
                sendCustomMessage(invited, "<color:#00E5E5>Die Einladung von<gold> " + inviter.getName() + " </gold>an dich ist abgelaufen.</color:#00E5E5>");
                invitesTasks.remove(key);
            }
        }.runTaskLater(MiniGamesV4.getInstance(), 1200L));

    }

    //A method witch checks if the inviter can invite the invited player to play a game
    public static void canInvitePlayer(Player inviter, Player invited, String game) {

        //Check if the Player wants to Invite himself
        if (inviter.getName().equals(invited.getName())) {
            sendNoInviteYourselfMessage(inviter);
            return;
        }

        //Check if the Player wants to get Invited
        if (ToggleInvitesStorage.isToggle.containsKey(invited)) {
            sendCustomMessage(inviter, "<red>Der Spieler hat Einladungen deaktiviert.</red>");
            return;
        }

        //Check if the inviter is arlready in a Game
        if (gameInfo.containsKey(inviter.getPlayer()) || gameInfo.containsValue(inviter.getPlayer())) {
            sendAlreadyInGameMessage(inviter);
            return;
        }

        //Check if the Player is already in a Game
        if (gameInfo.containsKey(invited.getPlayer()) || gameInfo.containsValue(invited.getPlayer())) {
            sendCustomMessage(inviter, "<red>Der Spieler befindet sich gerade in einem Spiel.</red>");
            return;
        }

        sendCustomMessage(inviter, "<color:#00E5E5>Du hast<gold> " + invited.getName() + "</gold> eingeladen.</color:#00E5E5>");

        //This is a message with a Command implemented it need to be made in this way beause RichMasse does not support click events
        MiniMessage mm = MiniMessage.miniMessage();
        Component askToPlayMessage = mm.deserialize(prefix() +
                "<color:#00E5E5>Möchtest du mit <gold>" + inviter.getName() + " </gold>" + game + " spielen?</color:#00E5E5> " +
                "<green><bold><click:run_command:'/minigames accept " + inviter.getName() + "'>[Ja]</click></bold></green> " +
                "<red><bold><click:run_command:'/minigames declined " + inviter.getName() + "'>[Nein]</click></bold></red>"
        );

        //Add the invite to the HashMap
        addInvite(inviter, invited, game);
        //Play a sound for the target player so he can Check if he got invited
        Location targetPlayerPos = invited.getLocation();
        invited.sendMessage(askToPlayMessage);
        invited.playSound(targetPlayerPos, Sound.ENTITY_CHICKEN_EGG, 1F, 1F);

    }


    //This method Checks if the invited player who accepts the invite  if the game of the inviter is already started or not
    public static void canGameStart(Player inviter, Player invited) {

        if (gameInfo.containsKey(inviter.getPlayer()) || gameInfo.containsValue(inviter.getPlayer())) {
            sendCustomMessage(invited, "<red>Du bist bereits in einem Spiel.</red>");
            return;
        }

        //Checks if the player who send the invite is already in a game
        if (gameInfo.containsKey(inviter.getPlayer()) || gameInfo.containsKey(invited.getPlayer())) {
            sendCustomMessage(invited, "<red>Der Spieler befindet sich gerade in einem Spiel.</red>");
            return;
        }
        //Create a player key
        PlayerKey key = new PlayerKey(String.valueOf(inviter.getPlayer()), String.valueOf(invited.getPlayer()));
        if (invitesManager.containsKey(key)) {
            //I use switch for up comming games
            switch (invitesManager.get(key)) {
                case "TicTacToe":
                    TicTacToeGame.openTicTacToe(inviter, invited);
                    break;
                case "RPS":
                    RPSGame.startRPSGame(inviter, invited);
            }

            sendCustomMessage(inviter, "<color:#00E5E5>Die Einladung wurde von<gold> " + invited.getName() + " </gold>angenommen!</color:#00E5E5>");

            Location inviterPos = inviter.getLocation();
            Location invitedPos = invited.getLocation();
            //Plays a Start sound for both players
            inviter.playSound(inviterPos, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            invited.playSound(invitedPos, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            //All the Invites will be removed from the HashMap from both players
            removeAllInvites(inviter.getPlayer(), invited.getPlayer());
            return;
        }
        sendCustomMessage(invited, "<red>Du hast keine Einladung mehr.</red>");
    }


    //This method removes the invite from the HashMap and cancels the task
    public static void removeInvite(Player inviter, Player invited) {
        PlayerKey key = new PlayerKey(String.valueOf(inviter.getPlayer()), String.valueOf(invited.getPlayer()));
        if (invitesManager.containsKey(key)) {
            invitesManager.remove(key);
            BukkitTask task = invitesTasks.get(key);
            //The Runnable will be cancelled if it is not arleady cancelled and the key will be removed
            if (task != null) {
                task.cancel();
                invitesTasks.remove(key);
            }
            sendCustomMessage(inviter, "<color:#00E5E5>Die Einladung wurde von<gold> " + invited.getName() + " </gold>abgelehnt.</color:#00E5E5>");
            return;
        }
        sendCustomMessage(invited, "<red>Du hast keine Einladung mehr.</red>");
    }


    //This method removes all the invites from the HashMap and cancels the task if the game is started
    public static void removeAllInvites(Player inviter, Player invited) {
        gameInfo.put(inviter, invited);
        gameInfo.put(invited, inviter);
        //Checks every slot in the HashMap if the inviter is the same as the one who invited
        Iterator<Map.Entry<PlayerKey, String>> iterator = invitesManager.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<PlayerKey, String> entry = iterator.next();
            PlayerKey key = entry.getKey();
            //if somthing is found the key will be removed
            if (key.getInviter().equals(String.valueOf(inviter))) {
                iterator.remove();
                BukkitTask task = invitesTasks.get(key);
                //The Runnable will be cancelled if it is not arleady cancelled and the key will be removed
                if (task != null) {
                    task.cancel();
                    invitesTasks.remove(key);
                }
            }
        }
    }


    //This method Create a player Key from both players
    public static class PlayerKey {
        private final String inviter;
        private final String invited;

        //Getter for invited
        public String getInviter() {
            return inviter;
        }

        public PlayerKey(String inviter, String invited) {
            this.inviter = inviter;
            this.invited = invited;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PlayerKey that = (PlayerKey) obj;
            return inviter.equals(that.inviter) && invited.equals(that.invited);
        }

        @Override
        public int hashCode() {
            return Objects.hash(inviter, invited);
        }
    }
}
