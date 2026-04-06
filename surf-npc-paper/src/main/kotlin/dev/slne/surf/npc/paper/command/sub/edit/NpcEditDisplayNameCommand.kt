package dev.slne.surf.npc.paper.command.sub.edit

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.jorel.commandapi.kotlindsl.textArgument
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.paper.command.argument.npcArgument
import dev.slne.surf.npc.paper.util.PermissionRegistry
import dev.slne.surf.npc.paper.util.miniMessage

fun CommandAPICommand.npcEditDisplayNameCommand() = subcommand("displayname") {
    withPermission(PermissionRegistry.COMMAND_NPC_EDIT_DISPLAYNAME)
    npcArgument("npc")
    textArgument("displayName")
    playerExecutor { player, args ->
        val npc: Npc by args
        val displayName: String by args

        val name = miniMessage.deserialize(displayName)

        npc.addProperty(
            NpcProperty(
                NpcProperty.Internal.DISPLAYNAME,
                name,
                NpcPropertyType.Types.COMPONENT_TYPE
            )
        )

        npc.refresh()

        player.sendText {
            appendSuccessPrefix()
            success("Der Anzeigename des Npc ")
            variableValue(npc.uniqueName)
            success(" wurden aktualisiert.")
        }
    }
}