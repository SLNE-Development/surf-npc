package dev.slne.surf.npc.bukkit.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.bukkit.command.sub.NpcCreateCommand
import dev.slne.surf.npc.bukkit.command.sub.NpcDeleteCommand
import dev.slne.surf.npc.bukkit.command.sub.NpcInfoCommand
import dev.slne.surf.npc.bukkit.command.sub.NpcListCommand
import dev.slne.surf.npc.bukkit.command.sub.NpcRefreshCommand
import dev.slne.surf.npc.bukkit.command.sub.NpcTeleportHereCommand
import dev.slne.surf.npc.bukkit.command.sub.NpcTeleportToCommand
import dev.slne.surf.npc.bukkit.command.sub.edit.NpcEditCommand
import dev.slne.surf.npc.bukkit.command.sub.files.NpcExportAllCommand
import dev.slne.surf.npc.bukkit.command.sub.files.NpcExportCommand
import dev.slne.surf.npc.bukkit.command.sub.files.NpcImportAllCommand
import dev.slne.surf.npc.bukkit.command.sub.files.NpcImportCommand
import dev.slne.surf.npc.bukkit.command.sub.files.NpcReloadFromDiskCommand
import dev.slne.surf.npc.bukkit.command.sub.files.NpcSaveToDiskCommand
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