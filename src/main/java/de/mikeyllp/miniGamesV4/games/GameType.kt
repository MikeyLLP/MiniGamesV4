package de.mikeyllp.miniGamesV4.games

import de.mikeyllp.miniGamesV4.plugin

enum class GameType(val argsName: String) {

    HIDE_AND_SEEK("hideandseek"),
    RPS("rps"),
    TIC_TAC_TOE("tictactoe");

    companion object {
        fun fromArgsName(name: String) = entries.find { it.argsName.equals(name, ignoreCase = true) }
    }

    fun isEnabled(): Boolean {
        return when (this) {
            HIDE_AND_SEEK -> plugin.config.getBoolean("HideAndSeek")
            RPS -> plugin.config.getBoolean("RPS")
            TIC_TAC_TOE -> plugin.config.getBoolean("TicTacToe")
        }
    }
}