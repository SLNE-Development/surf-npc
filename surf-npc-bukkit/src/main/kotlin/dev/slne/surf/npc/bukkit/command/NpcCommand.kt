package dev.slne.surf.npc.bukkit.command

import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.slne.surf.npc.bukkit.command.sub.*
import dev.slne.surf.npc.bukkit.command.sub.edit.npcEditCommand
import dev.slne.surf.npc.bukkit.command.sub.files.*
import dev.slne.surf.npc.bukkit.command.sub.property.npcPropertyCommand
import dev.slne.surf.npc.bukkit.util.PermissionRegistry

fun npcCommand() = commandAPICommand("npc") {
    withPermission(PermissionRegistry.COMMAND_NPC)

    npcCreateCommand()
    npcDeleteCommand()
    npcInfoCommand()
    npcListCommand()
    npcEditCommand()
    npcTeleportToCommand()
    npcTeleportHereCommand()
    npcPropertyCommand()
    npcExportCommand()
    npcExportAllCommand()
    npcImportCommand()
    npcImportAllCommand()
    npcReloadFromDiskCommand()
    npcSaveToDiskCommand()
    npcRefreshCommand()
    npcAnimateCommand()

    npcVersionCommand()
}