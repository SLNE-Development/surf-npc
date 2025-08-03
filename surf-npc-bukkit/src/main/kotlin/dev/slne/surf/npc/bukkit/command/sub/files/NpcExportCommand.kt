package dev.slne.surf.npc.bukkit.command.sub.files

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.core.service.storageService
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcExportCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_EXPORT)
        npcArgument("npc")
        playerExecutor { player, args ->
            val npc: Npc by args

            storageService.export(npc)

            player.sendText {
                appendPrefix()
                success("Der NPC '${npc.uniqueName}' wurde erfolgreich exportiert.")
            }
        }
    }
}