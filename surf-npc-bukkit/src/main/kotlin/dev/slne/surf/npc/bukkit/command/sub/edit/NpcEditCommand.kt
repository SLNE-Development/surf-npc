package dev.slne.surf.npc.bukkit.command.sub.edit

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.bukkit.util.PermissionRegistry

fun CommandAPICommand.npcEditCommand() = subcommand("edit") {
    withPermission(PermissionRegistry.COMMAND_NPC_EDIT)

    npcEditRotationCommand()
    npcEditSkinCommand()
    npcEditDisplayNameCommand()
    npcEditPoseCommand()
}