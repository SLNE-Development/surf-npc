package dev.slne.surf.npc.bukkit.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcRefreshCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_REFRESH)
        npcArgument("npc")
        playerExecutor { player, args ->
            val npc: Npc by args

            npc.refresh()

            player.sendText {
                appendPrefix()
                success("Der NPC ")
                variableValue(npc.uniqueName)
                success(" wurde aktualisiert.")
            }
        }
    }
}