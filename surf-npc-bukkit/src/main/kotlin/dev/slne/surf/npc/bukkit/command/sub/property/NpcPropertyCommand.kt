package dev.slne.surf.npc.bukkit.command.sub.property

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.bukkit.util.PermissionRegistry

fun CommandAPICommand.npcPropertyCommand() = subcommand("property") {
    withPermission(PermissionRegistry.COMMAND_NPC_PROPERTY)

    npcPropertyAddCommand()
    npcPropertyRemoveCommand()
    npcPropertyListCommand()
}