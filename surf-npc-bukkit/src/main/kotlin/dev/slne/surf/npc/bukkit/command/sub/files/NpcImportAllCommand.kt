package dev.slne.surf.npc.bukkit.command.sub.files

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand

import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.core.service.storageService
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.npcImportAllCommand() = subcommand("import-all") {
    withPermission(PermissionRegistry.COMMAND_NPC_IMPORT_ALL)
    playerExecutor { player, args ->
        val amount = storageService.importAll()

        player.sendText {
            if (amount > 0) {
                appendSuccessPrefix()
                success("Du hast erfolgreich $amount NPCs importiert.")
            } else {
                appendErrorPrefix
                error("Es wurden keine NPCs zum Import gefunden.")
            }
        }
    }
}