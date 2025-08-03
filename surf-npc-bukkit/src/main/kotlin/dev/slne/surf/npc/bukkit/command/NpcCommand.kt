package dev.slne.surf.npc.bukkit.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.bukkit.command.sub.*
import dev.slne.surf.npc.bukkit.command.sub.edit.NpcEditCommand
import dev.slne.surf.npc.bukkit.command.sub.files.*
import dev.slne.surf.npc.bukkit.command.sub.property.NpcPropertyCommand
import dev.slne.surf.npc.bukkit.util.PermissionRegistry

class NpcCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC)
        subcommand(NpcCreateCommand("create"))
        subcommand(NpcDeleteCommand("delete"))
        subcommand(NpcInfoCommand("info"))
        subcommand(NpcListCommand("list"))
        subcommand(NpcEditCommand("edit"))
        subcommand(NpcTeleportToCommand("teleport"))
        subcommand(NpcTeleportHereCommand("teleporthere"))
        subcommand(NpcPropertyCommand("property"))
        subcommand(NpcExportCommand("export"))
        subcommand(NpcExportAllCommand("exportall"))
        subcommand(NpcImportCommand("import"))
        subcommand(NpcImportAllCommand("importall"))
        subcommand(NpcReloadFromDiskCommand("loadFromDisk"))
        subcommand(NpcSaveToDiskCommand("saveToDisk"))
        subcommand(NpcRefreshCommand("refresh"))
    }
}