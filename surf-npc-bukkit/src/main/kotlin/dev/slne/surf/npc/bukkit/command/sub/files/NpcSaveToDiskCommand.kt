package dev.slne.surf.npc.bukkit.command.sub.files

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor

import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.core.service.storageService
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcSaveToDiskCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_SAVE_TO_DISK)
        playerExecutor { player, args ->
            val amount = storageService.saveToDisk()

            player.sendText {
                appendPrefix()
                if (amount > 0) {
                    success("Es wurden $amount NPCs erfolgreich auf die Festplatte gespeichert.")
                } else {
                    error("Es wurden keine NPCs auf die Festplatte gespeichert.")
                }
            }
        }
    }
}