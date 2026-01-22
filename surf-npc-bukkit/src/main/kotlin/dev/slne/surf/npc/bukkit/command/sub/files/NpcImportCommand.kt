package dev.slne.surf.npc.bukkit.command.sub.files

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.subcommand

import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.core.service.storageService
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.npcImportCommand() = subcommand("import") {
    withPermission(PermissionRegistry.COMMAND_NPC_IMPORT)
    stringArgument("fileName")
    playerExecutor { player, args ->
        val fileName: String by args
        val result = storageService.import(fileName)

        player.sendText {
            if (result) {
                appendSuccessPrefix()
                success("Der NPC aus der Datei '$fileName' wurde erfolgreich importiert.")
            } else {
                appendErrorPrefix()
                error("Fehler beim Importieren der NPCs aus der Datei '$fileName'.")
            }
        }
    }
}