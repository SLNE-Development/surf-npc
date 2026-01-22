package dev.slne.surf.npc.bukkit.command.sub.files

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand

import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.core.service.storageService
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.npcReloadFromDiskCommand() = subcommand("reload-from-disk") {
    withPermission(PermissionRegistry.COMMAND_NPC_RELOAD_FROM_DISK)
    playerExecutor { player, args ->
        val amount = storageService.reloadFromDisk()

        player.sendText {
            if (amount > 0) {
                appendSuccessPrefix()
                success("Es wurden $amount NPCs erfolgreich von der Festplatte neu geladen.")
            } else {
                appendErrorPrefix()
                error("Es wurden keine NPCs von der Festplatte neu geladen.")
            }
        }
    }
}