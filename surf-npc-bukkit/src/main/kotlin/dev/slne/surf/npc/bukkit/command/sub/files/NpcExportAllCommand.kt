package dev.slne.surf.npc.bukkit.command.sub.files

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand

import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.core.service.storageService
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.npcExportAllCommand() = subcommand("export-all") {
    withPermission(PermissionRegistry.COMMAND_NPC_EXPORT_ALL)
    playerExecutor { player, _ ->
        val amount = storageService.exportAll()

        player.sendText {
            if (amount > 0) {
                appendSuccessPrefix()
                success("Alle NPCs wurden erfolgreich exportiert. ($amount)")
            } else {
                appendErrorPrefix()
                error("Es wurden keine NPCs zum Export gefunden.")
            }
        }
    }
}