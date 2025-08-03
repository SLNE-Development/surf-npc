package dev.slne.surf.npc.bukkit.command.sub.edit

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.bukkit.util.PermissionRegistry

class NpcEditCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_EDIT)

        subcommand(NpcEditRotationCommand("rotation"))
        subcommand(NpcEditSkinCommand("skin"))
        subcommand(NpcEditDisplayNameCommand("displayname"))
    }
}