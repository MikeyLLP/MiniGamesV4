package de.mikeyllp.miniGamesV4.storage

import de.mikeyllp.miniGamesV4.MiniGamesV4
import de.mikeyllp.miniGamesV4.games.GameInvite
import de.mikeyllp.miniGamesV4.games.GameType
import de.mikeyllp.miniGamesV4.games.rps.RPSGame
import de.mikeyllp.miniGamesV4.games.tictactoe.TicTacToeGame
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.invites
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.invitesTasks
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage.runningGames
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import dev.slne.surf.surfapi.core.api.util.object2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.objectListOf
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*
import java.util.function.Consumer

object InvitePlayerStorage {
    val invites = objectListOf<GameInvite>()
    val runningGames = object2ObjectMapOf<UUID, UUID>()

    //Deletes all invites after X seconds
    val invitesTasks: MutableMap<PlayerKey?, BukkitTask?> = HashMap<PlayerKey?, BukkitTask?>()


    fun isIngame(player: Player) = runningGames.any{
        it.key == player.uniqueId || it.value == player.uniqueId
    }


    fun addInvite(inviter: Player, invited: Player, game: GameType) {
        if(runningGames.containsKey(invited.uniqueId)) {
            MessageUtils.sendMessage(inviter, "<RED>Der Spieler befindet sich gerade in einem Spiel.</RED>")
            return
        }

        invites.add(GameInvite(
            inviter.uniqueId,
            invited.uniqueId,
            game
        ))

        //A Runnable is created to remove the invite after 60 seconds
        invitesTasks.put(key, object : BukkitRunnable() {
            override fun run() {
                removeInvite(inviter, invited)
                MessageUtils.sendMessage(
                    inviter,
                    "<color:#00E5E5>Die Einladung von dir an<gold> " + invited.getName() + " </gold>ist abgelaufen.</color:#00E5E5>"
                )
                MessageUtils.sendMessage(
                    invited,
                    "<color:#00E5E5>Die Einladung von<gold> " + inviter.getName() + " </gold>an dich ist abgelaufen.</color:#00E5E5>"
                )
                invitesTasks.remove(key)
            }
        }.runTaskLater(MiniGamesV4.Companion.getInstance(), 1200L))
    }

    // A method witch checks if the inviter can invite the invited player to play a game
    fun canInvitePlayer(inviter: Player, invited: Player, game: GameType) {
        // Check if the Player wants to Invite himself
        /*if (inviter.getName().equals(invited.getName())) {
            sendNoInviteYourselfMessage(inviter);
            return;
        }*/

        // Check if the Player wants to get Invited

        storage.checkToggle(invited, Consumer { canBeInvited: Boolean? ->
            if (!canBeInvited!!) {
                MessageUtils.sendMessage(inviter, "<red>Der Spieler hat Einladungen deaktiviert.</red>")
                return@checkToggle
            }
            // Check if the inviter is arlready in a Game
            if (gameInfo.containsKey(inviter.getPlayer()) || gameInfo.containsValue(inviter.getPlayer())) {
                MessageUtils.sendAlreadyInGameMessage(inviter)
                return@checkToggle
            }

            // Check if the Player is already in a Game
            if (gameInfo.containsKey(invited.getPlayer()) || gameInfo.containsValue(invited.getPlayer())) {
                MessageUtils.sendMessage(inviter, "<red>Der Spieler befindet sich gerade in einem Spiel.</red>")
                return@checkToggle
            }

            MessageUtils.sendMessage(
                inviter,
                "<color:#00E5E5>Du hast<gold> " + invited.getName() + "</gold> eingeladen.</color:#00E5E5>"
            )

            //This is a message with a Command implemented it need to be made in this way beause RichMasse does not support click events
            val mm = MiniMessage.miniMessage()
            val askToPlayMessage = mm.deserialize(
                MessageUtils.prefix() +
                        "<color:#00E5E5>Möchtest du mit <gold>" + inviter.getName() + " </gold>" + game + " spielen?</color:#00E5E5> " +
                        "<green><bold><click:run_command:'/minigames accept " + inviter.getName() + "'>[Ja]</click></bold></green> " +
                        "<red><bold><click:run_command:'/minigames declined " + inviter.getName() + "'>[Nein]</click></bold></red>"
            )

            // Add the invite to the HashMap
            addInvite(inviter, invited, game)
            // Play a sound for the target player so he can Check if he got invited
            val targetPlayerPos = invited.getLocation()
            invited.sendMessage(askToPlayMessage)
            invited.playSound(targetPlayerPos, Sound.ENTITY_CHICKEN_EGG, 1f, 1f)
        })
        /*if (storage.checkToggle(invited)) {
            sendCustomMessage(inviter, "<red>Der Spieler hat Einladungen deaktiviert.</red>");
            return;
        }*/
    }


    // This method Checks if the invited player who accepts the invite  if the game of the inviter is already started or not
    fun canGameStart(inviter: Player, invited: Player) {
        if (gameInfo.containsKey(inviter.getPlayer()) || gameInfo.containsValue(inviter.getPlayer())) {
            MessageUtils.sendMessage(invited, "<red>Du bist bereits in einem Spiel.</red>")
            return
        }

        // Checks if the player who send the invite is already in a game
        if (gameInfo.containsKey(inviter.getPlayer()) || gameInfo.containsKey(invited.getPlayer())) {
            MessageUtils.sendMessage(invited, "<red>Der Spieler befindet sich gerade in einem Spiel.</red>")
            return
        }
        // Create a player key
        val key = PlayerKey(inviter.getPlayer().toString(), invited.getPlayer().toString())
        if (invites.containsKey(key)) {
            // I use switch for up comming games
            when (invites.get(key)) {
                "TicTacToe" -> TicTacToeGame.openTicTacToe(inviter, invited)
                "RPS" -> RPSGame.Companion.startRPSGame(inviter, invited)
            }

            MessageUtils.sendMessage(
                inviter,
                "<color:#00E5E5>Die Einladung wurde von<gold> " + invited.getName() + " </gold>angenommen!</color:#00E5E5>"
            )

            val inviterPos = inviter.getLocation()
            val invitedPos = invited.getLocation()
            // Plays a Start sound for both players
            inviter.playSound(inviterPos, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
            invited.playSound(invitedPos, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
            // All the Invites will be removed from the HashMap from both players
            removeAllInvites(inviter.getPlayer(), invited.getPlayer())
            return
        }
        MessageUtils.sendMessage(invited, "<red>Du hast keine Einladung mehr.</red>")
    }

    // This method removes the invite from the HashMap and cancels the task
    fun removeInvite(inviter: Player, invited: Player) {
        val result = invites.removeIf { invite: GameInvite ->
            invite.inviter == inviter.uniqueId && invite.invited == invited.uniqueId
        }

        if (result) {
            val task = invitesTasks.get(key)
            // The Runnable will be cancelled if it is not arleady cancelled and the key will be removed
            if (task != null) {
                task.cancel()
                invitesTasks.remove(key)
            }
            MessageUtils.sendMessage(
                inviter,
                "<color:#00E5E5>Die Einladung wurde von<gold> " + invited.getName() + " </gold>abgelehnt.</color:#00E5E5>"
            )
            return
        }

        MessageUtils.sendMessage(invited, "<red>Du hast keine Einladung mehr.</red>")
    }

    // This method removes all the invites from the HashMap and cancels the task if the game is started
    fun removeAllInvites(inviter: Player?, invited: Player?) {
        gameInfo.put(inviter, invited)
        gameInfo.put(invited, inviter)
        //Checks every slot in the HashMap if the inviter is the same as the one who invited
        val iterator = invites.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val key = entry.key
            // if somthing is found the key will be removed
            if (key.inviter == inviter.toString()) {
                iterator.remove()
                val task = invitesTasks.get(key)
                // The Runnable will be cancelled if it is not arleady cancelled and the key will be removed
                if (task != null) {
                    task.cancel()
                    invitesTasks.remove(key)
                }
            }
        }
    }
