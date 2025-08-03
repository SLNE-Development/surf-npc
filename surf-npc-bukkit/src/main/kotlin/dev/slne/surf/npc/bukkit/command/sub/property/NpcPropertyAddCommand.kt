package dev.slne.surf.npc.bukkit.command.sub.property

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.command.argument.npcPropertyTypeArgument
import dev.slne.surf.npc.bukkit.property.BukkitNpcProperty
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcPropertyAddCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_PROPERTY_ADD)
        npcArgument("npc")
        stringArgument("key")
        stringArgument("value")
        npcPropertyTypeArgument("propertyType")
        playerExecutor { player, args ->
            val npc: Npc by args
            val key: String by args
            val value: String by args
            val propertyType: NpcPropertyType by args

            val exists = npc.hasProperty(key)

            npc.addProperty(BukkitNpcProperty(
                key, propertyType.decode(value), propertyType
            ))

            player.sendText {
                appendPrefix()

                if(exists) {
                    success("Die Property '${key}' wurde erfolgreich dem NPC '${npc.uniqueName}' neu gesetzt.")
                } else {
                    success("Die Property '${key}' wurde erfolgreich zum NPC '${npc.uniqueName}' hinzugefügt.")
                }
            }
        }
    }
}