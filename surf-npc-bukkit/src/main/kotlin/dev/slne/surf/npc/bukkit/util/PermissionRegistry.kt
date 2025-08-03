package dev.slne.surf.npc.bukkit.util

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object PermissionRegistry : PermissionRegistry() {
    const val PREFIX = "surf.npc"

    val COMMAND_NPC = create("$PREFIX.command")
    val COMMAND_NPC_CREATE = create("$PREFIX.command.create")
    val COMMAND_NPC_DELETE = create("$PREFIX.command.delete")
    val COMMAND_NPC_REFRESH = create("$PREFIX.command.refresh")

    val COMMAND_NPC_LIST = create("$PREFIX.command.list")
    val COMMAND_NPC_INFO = create("$PREFIX.command.info")

    val COMMAND_NPC_EDIT = create("$PREFIX.command.edit")
    val COMMAND_NPC_EDIT_ROTATION = create("$PREFIX.command.edit.rotation")
    val COMMAND_NPC_EDIT_SKIN = create("$PREFIX.command.edit.skin")
    val COMMAND_NPC_DISPLAYNAME = create("$PREFIX.command.edit.displayname")

    val COMMAND_NPC_TELEPORT_TO = create("$PREFIX.command.teleport")
    val COMMAND_NPC_TELEPORT_HERE = create("$PREFIX.command.teleporthere")

    val COMMAND_NPC_PROPERTY = create("$PREFIX.command.property")
    val COMMAND_NPC_PROPERTY_ADD = create("$PREFIX.command.property.add")
    val COMMAND_NPC_PROPERTY_REMOVE = create("$PREFIX.command.property.remove")
    val COMMAND_NPC_PROPERTY_LIST = create("$PREFIX.command.property.list")

    val COMMAND_NPC_IMPORT = create("$PREFIX.command.import")
    val COMMAND_NPC_IMPORT_ALL = create("$PREFIX.command.importall")
    val COMMAND_NPC_EXPORT = create("$PREFIX.command.export")
    val COMMAND_NPC_EXPORT_ALL = create("$PREFIX.command.exportall")
    val COMMAND_NPC_RELOAD_FROM_DISK = create("$PREFIX.command.loadFromDisk")
    val COMMAND_NPC_SAVE_TO_DISK = create("$PREFIX.command.saveToDisk")
}
