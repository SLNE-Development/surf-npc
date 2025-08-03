package dev.slne.surf.npc.bukkit.command.sub.files

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor

import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.core.service.storageService
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcReloadFromDiskCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_RELOAD_FROM_DISK)
        playerExecutor { player, args ->
            val amount = storageService.reloadFromDisk()

            player.sendText {
                appendPrefix()
                if (amount > 0) {
                    success("Es wurden $amount NPCs erfolgreich von der Festplatte neu geladen.")
                } else {
                    error("Es wurden keine NPCs von der Festplatte neu geladen.")
                }
            }
        }
    }
}