package dev.slne.surf.npc.bukkit.command.sub.files

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor

import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.core.service.storageService
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcExportAllCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_EXPORT_ALL)
        playerExecutor { player, args ->
            val amount = storageService.exportAll()

            player.sendText {
                appendPrefix()
                if (amount > 0) {
                    success("Alle NPCs wurden erfolgreich exportiert. ($amount)")
                } else {
                    error("Es wurden keine NPCs zum Export gefunden.")
                }
            }
        }
    }
}