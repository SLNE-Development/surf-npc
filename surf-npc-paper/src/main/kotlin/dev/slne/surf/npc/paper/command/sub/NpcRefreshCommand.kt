package dev.slne.surf.npc.paper.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.paper.command.argument.npcArgument
import dev.slne.surf.npc.paper.util.PermissionRegistry

fun CommandAPICommand.npcRefreshCommand() = subcommand("refresh") {
    withPermission(PermissionRegistry.COMMAND_NPC_REFRESH)
    npcArgument("npc")
    playerExecutor { player, args ->
        val npc: Npc by args

        npc.refresh()

        player.sendText {
            appendSuccessPrefix()
            success("Der NPC ")
            variableValue(npc.uniqueName)
            success(" wurde aktualisiert.")
        }
    }
}