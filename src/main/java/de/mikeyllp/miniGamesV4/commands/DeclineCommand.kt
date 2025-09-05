package de.mikeyllp.miniGamesV4.commands

import de.mikeyllp.miniGamesV4.messages.MessageUtils
import de.mikeyllp.miniGamesV4.messages.Translator
import de.mikeyllp.miniGamesV4.permission.MinigamesPermissionRegistry
import de.mikeyllp.miniGamesV4.storage.InvitePlayerStorage
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import org.bukkit.entity.Player

fun CommandAPICommand.declineCommand() = subcommand("decline") {
    withPermission(MinigamesPermissionRegistry.COMMAND_DECLINE)
    playerArgument("toDecline")

    playerExecutor { player, args ->
        val toDecline: Player by args

        if (player == toDecline) {
            MessageUtils.sendMessage(player, Translator.translatable("warning-message.no-invite-yourself"))
            return@playerExecutor
        }

        InvitePlayerStorage.removeInvite(toDecline, player)
    }
}

