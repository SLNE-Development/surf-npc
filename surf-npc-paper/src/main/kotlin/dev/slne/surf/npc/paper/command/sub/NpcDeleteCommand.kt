package dev.slne.surf.npc.paper.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.paper.command.argument.npcArgument
import dev.slne.surf.npc.paper.controller.npcController
import dev.slne.surf.npc.paper.util.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.npcDeleteCommand() = subcommand("delete") {
    withPermission(PermissionRegistry.COMMAND_NPC_DELETE)
    npcArgument("npc")
    playerExecutor { player, args ->
        val npc: Npc by args

        npc.delete()

        if (npcController.getNpc(npc.id) == null) {
            player.sendText {
                appendSuccessPrefix()
                success("Der Npc ")
                variableValue(npc.uniqueName)
                success(" wurde gelöscht.")
            }
        } else {
            player.sendText {
                appendErrorPrefix()
                error("Der Npc ${npc.uniqueName} konnte nicht gelöscht werden.")
            }
        }
    }
}