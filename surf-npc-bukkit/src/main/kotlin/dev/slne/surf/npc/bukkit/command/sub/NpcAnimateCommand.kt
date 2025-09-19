package dev.slne.surf.npc.bukkit.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.animation.NpcAnimationType
import dev.slne.surf.npc.bukkit.command.argument.npcAnimationTypeArgument
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.npcAnimateCommand() = subcommand("animate") {
    withPermission(PermissionRegistry.COMMAND_NPC_ANIMATE)
    npcArgument("npc")
    npcAnimationTypeArgument("animationType")
    playerExecutor { player, args ->
        val npc: Npc by args
        val animationType: NpcAnimationType by args

        npcController.playAnimation(npc, animationType)

        player.sendText {
            appendPrefix()
            success("Die Animation ")
            variableValue(animationType.name)
            success(" wurde für den Npc ")
            variableValue(npc.uniqueName)
            success(" abgespielt.")
        }
    }
}