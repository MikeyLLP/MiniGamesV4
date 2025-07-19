package de.mikeyllp.miniGamesV4.utils

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object MinigamesPermissionRegistry : PermissionRegistry() {

    private const val PREFIX = "minigamesv4"
    private const val COMMAND_PREFIX = "$PREFIX.command"

    val COMMAND_CLEAR = create("$COMMAND_PREFIX.clear")
    val COMMAND_ACCEPT = create("$COMMAND_PREFIX.accept")
    val COMMAND_DECLINE = create("$COMMAND_PREFIX.decline")
    val COMMAND_INVITE = create("$COMMAND_PREFIX.invite")
    val COMMAND_HELP = create("$COMMAND_PREFIX.help")
    val COMMAND_ADMIN_HELP = create("$COMMAND_PREFIX.admin-help")
    val COMMAND_MINIGAMES_MENU = create("$COMMAND_PREFIX.minigames-menu")
    val COMMAND_QUIT = create("$COMMAND_PREFIX.quit")

}