package de.mikeyllp.miniGamesV4.commands

import de.mikeyllp.miniGamesV4.games.hideandseek.storage.HideAndSeekGameGroups
import de.mikeyllp.miniGamesV4.games.hideandseek.utils.RemovePlayersHideAndSeek
import de.mikeyllp.miniGamesV4.games.rps.RPSGame
import de.mikeyllp.miniGamesV4.messages.MessageUtils
import de.mikeyllp.miniGamesV4.messages.Translator
import de.mikeyllp.miniGamesV4.permission.MinigamesPermissionRegistry
import de.mikeyllp.miniGamesV4.storage.ClickInviteStorage
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage
import de.mikeyllp.miniGamesV4.utils.ClickInviteUtils
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

        if (ClickInviteStorage.Companion.enableListener.containsKey(player)) {

            val mm = MiniMessage.miniMessage()
            val message = mm.deserialize("")

            player.showTitle(
                Title.title(
                    Translator.translatable("special-message.click-invite-disable"),
                    message, Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
                )
            )
            ClickInviteUtils.removePlayer(player)
            return@playerExecutor
        }

        if (InvitePlayerStorage.runningGames.containsKey(player.uniqueId)) {

            val opponentUuid: UUID = InvitePlayerStorage.runningGames[player.uniqueId]!!
            val opponent: Player = Bukkit.getPlayer(opponentUuid)!!

            MessageUtils.sendMessage(player, Translator.translatable("warning-message.game-quit"))
            MessageUtils.sendMessage(opponent, Translator.translatable("warning-message.player-quit"))

            RPSGame.Companion.removePlayersFromList(player, opponent)
            return@playerExecutor
        }



        if (RemovePlayersHideAndSeek.playerRemove(player, "quit")) {
            InvitePlayerStorage.runningGames.remove(player.uniqueId)
            return@playerExecutor
        } else {

            if (!HideAndSeekGameGroups.Companion.listUntilX.contains(player)) {
                MessageUtils.sendMessage(player, Translator.translatable("warning-message.nothing-to-quit"))
                return@playerExecutor
            }

            InvitePlayerStorage.runningGames.remove(player.uniqueId)
            HideAndSeekGameGroups.Companion.listUntilX.removeIf { value: Player? -> value == player }
            MessageUtils.sendMessage(player, Translator.translatable("warning-message.queue-quit"))
        }


    }

}
