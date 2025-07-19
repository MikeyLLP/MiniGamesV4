package de.mikeyllp.miniGamesV4.games

import java.util.*

data class GameInvite(
    val inviter: UUID,
    val invited: UUID,
    val gameName: GameType
)
