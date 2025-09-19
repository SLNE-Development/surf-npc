package dev.slne.surf.npc.bukkit.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.npcDeleteCommand() = subcommand("delete") {
    withPermission(PermissionRegistry.COMMAND_NPC_DELETE)
    npcArgument("npc")
    playerExecutor { player, args ->
        val npc: Npc by args

        if (npc.isFromPlugin()) {
            player.sendText {
                appendPrefix()
                error("Der Npc wurde von einem Plugin erstellt und kann daher nicht bearbeitet werden.")
            }
            return@playerExecutor
        }

        npc.delete()

        if (npcController.getNpc(npc.id) == null) {
            player.sendText {
                appendPrefix()
                success("Der Npc ")
                variableValue(npc.uniqueName)
                success(" wurde gelöscht.")
            }
        } else {
            player.sendText {
                appendPrefix()
                error("Der Npc ${npc.uniqueName} konnte nicht gelöscht werden.")
            }
        }
    }
}