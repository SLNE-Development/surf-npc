package dev.slne.surf.npc.bukkit.command.sub.property

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.bukkit.command.sub.edit.NpcEditDisplayNameCommand
import dev.slne.surf.npc.bukkit.command.sub.edit.NpcEditRotationCommand
import dev.slne.surf.npc.bukkit.command.sub.edit.NpcEditSkinCommand
import dev.slne.surf.npc.bukkit.util.PermissionRegistry

class NpcPropertyCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_PROPERTY)

        subcommand(NpcPropertyAddCommand("add"))
        subcommand(NpcPropertyRemoveCommand("remove"))
        subcommand(NpcPropertyListCommand("list"))
    }
}