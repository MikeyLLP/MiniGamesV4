package de.mikeyllp.miniGamesV4.commands

import de.mikeyllp.miniGamesV4.games.GameType
import de.mikeyllp.miniGamesV4.games.hideandseek.HideAndSeekGame
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage
import de.mikeyllp.miniGamesV4.utils.MessageUtils
import de.mikeyllp.miniGamesV4.utils.MinigamesPermissionRegistry
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.*
import org.bukkit.entity.Player

fun CommandAPICommand.inviteCommand() = subcommand("invite") {
    withPermission(MinigamesPermissionRegistry.COMMAND_INVITE)
    multiLiteralArgument("game", *GameType.entries.map { it.argsName }.toTypedArray())
    playerArgument("toInvite")

    playerExecutor { player, args ->
        val game: String by args
        val gameType = GameType.fromArgsName(game)!!

        val toInvite: Player by args

        if (InvitePlayerStorage.isIngame(player)) {
            MessageUtils.sendMessage(player, "warning-message.already-in-game.self")
            return@playerExecutor
        }

        if (InvitePlayerStorage.isIngame(toInvite)) {
            MessageUtils.sendMessage(player, "warning-message.already-in-game.other")
            return@playerExecutor
        }

        if (player == toInvite) {
            MessageUtils.sendMessage(player, "warning-message.no-invite-yourself")
            return@playerExecutor
        }

        if (gameType.isEnabled()) {
            MessageUtils.sendMessage(player, "warning-message.disabled-game")
            return@playerExecutor
        }

        when (gameType) {
            GameType.HIDE_AND_SEEK -> HideAndSeekGame.addPlayerToHAS(player)

            GameType.RPS, GameType.TIC_TAC_TOE -> InvitePlayerStorage.canInvitePlayer(player, toInvite, gameType)
        }
    }
}

