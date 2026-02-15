package dev.slne.surf.npc.paper.command.sub.property

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.paper.command.argument.npcArgument
import dev.slne.surf.npc.paper.command.argument.npcPropertyTypeArgument
import dev.slne.surf.npc.paper.util.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.npcPropertyAddCommand() = subcommand("add") {
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

        npc.addProperty(
            NpcProperty(
                key, propertyType.decode(value), propertyType
            )
        )

        player.sendText {
            appendSuccessPrefix()

            if (exists) {
                success("Die Property '${key}' wurde erfolgreich dem NPC '${npc.uniqueName}' neu gesetzt.")
            } else {
                success("Die Property '${key}' wurde erfolgreich zum NPC '${npc.uniqueName}' hinzugefügt.")
            }
        }
    }
}