package dev.slne.surf.npc.bukkit.command.sub.edit

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.NpcPose
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.command.argument.npcPoseArgument
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.npcEditPoseCommand() = subcommand("pose") {
    withPermission(PermissionRegistry.COMMAND_NPC_EDIT_POSE)
    npcArgument("npc")
    npcPoseArgument("pose")

    playerExecutor { player, args ->
        val npc: Npc by args
        val pose: NpcPose by args

        npc.setPose(pose)

        player.sendText {
            appendSuccessPrefix()
            success("Die Pose des Npcs ")
            variableValue(npc.uniqueName)
            success(" wurde auf ")
            variableValue(pose.name)
            success(" gesetzt.")
        }
    }
}