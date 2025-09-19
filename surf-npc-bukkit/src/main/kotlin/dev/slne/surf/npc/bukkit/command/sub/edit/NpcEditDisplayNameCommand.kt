package dev.slne.surf.npc.bukkit.command.sub.edit

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.jorel.commandapi.kotlindsl.textArgument
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.npc.property.BukkitNpcProperty
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.miniMessage
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.npcEditDisplayNameCommand() = subcommand("displayname") {
    withPermission(PermissionRegistry.COMMAND_NPC_DISPLAYNAME)
    npcArgument("npc")
    textArgument("displayName")
    playerExecutor { player, args ->
        val npc: Npc by args
        val displayName: String by args

        val name = miniMessage.deserialize(displayName)

        if (npc.isFromPlugin()) {
            player.sendText {
                appendPrefix()
                error("Der Npc wurde von einem Plugin erstellt und kann daher nicht bearbeitet werden.")
            }
            return@playerExecutor
        }

        npc.addProperty(
            BukkitNpcProperty(
                NpcProperty.Internal.DISPLAYNAME,
                name,
                propertyTypeRegistry.get(NpcPropertyType.Types.COMPONENT)
                    ?: return@playerExecutor
            )
        )

        npc.refresh()

        player.sendText {
            appendPrefix()
            success("Der Anzeigename des Npc ")
            variableValue(npc.uniqueName)
            success(" wurden aktualisiert.")
        }
    }
}