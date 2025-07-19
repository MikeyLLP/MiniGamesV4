package de.mikeyllp.miniGamesV4.commands

import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import de.mikeyllp.miniGamesV4.games.hideandseek.utils.removePlayersHideAndSeek
import de.mikeyllp.miniGamesV4.games.rps.RPSGame
import de.mikeyllp.miniGamesV4.plugin
import de.mikeyllp.miniGamesV4.storage.ClickInviteStorage
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage
import de.mikeyllp.miniGamesV4.utils.ClickInviteUtils
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import de.mikeyllp.miniGamesV4.utils.MinigamesPermissionRegistry
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.Duration
import java.util.*

fun CommandAPICommand.quitCommand() = subcommand("quit") {
    withPermission(MinigamesPermissionRegistry.COMMAND_QUIT)

    playerExecutor { player, args ->
        val langConfig = MessageUtils.getActiveLangConfig()

        if (ClickInviteStorage.Companion.enableListener.containsKey(player)) {
            val mm = MiniMessage.miniMessage()
            val miniGameComponent =
                mm.deserialize(langConfig.getString("special-message.click-invite-disable").toString())
            val message = mm.deserialize("")
            player.showTitle(
                Title.title(
                    miniGameComponent,
                    message, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
                )
            )
            ClickInviteUtils.removePlayer(player)
            return@playerExecutor
        }


        // Checks if the player is in a game
        if (InvitePlayerStorage.runningGames.containsKey(player.uniqueId)) {
            val opponentUuid: UUID = InvitePlayerStorage.runningGames[player.uniqueId]!!
            val opponent: Player = Bukkit.getPlayer(opponentUuid)!!
            MessageUtils.sendMessage(player, "warning-message.game-quit")
            MessageUtils.sendMessage(opponent, "warning-message.player-quit")

            //removes the inviter and invited from the maps
            RPSGame.Companion.removePlayersFromList(player, opponent)
            return@playerExecutor
        }



        if (removePlayersHideAndSeek.playerRemove(player, "quit", plugin)) {
            InvitePlayerStorage.runningGames.remove(player.uniqueId)
            return@playerExecutor
        }


        if (!HideAndSeekGameGroups.Companion.listUntilX.contains(player)) {
            MessageUtils.sendMessage(player, "warning-message.nothing-to-quit")
            return@playerExecutor
        }
        InvitePlayerStorage.runningGames.remove(player.uniqueId)
        HideAndSeekGameGroups.Companion.listUntilX.removeIf { value: Player? -> value == player }
        MessageUtils.sendMessage(player, "warning-message.queue-quit")


    }

}
