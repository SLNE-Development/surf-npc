package dev.slne.surf.npc.paper.command.sub.edit

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.paper.command.argument.npcArgument
import dev.slne.surf.npc.paper.command.argument.rotationTypeArgument
import dev.slne.surf.npc.paper.util.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.Location

fun CommandAPICommand.npcEditRotationCommand() = subcommand("rotation") {
    withPermission(PermissionRegistry.COMMAND_NPC_EDIT_ROTATION)
    npcArgument("npc")
    rotationTypeArgument("rotationType")
    playerExecutor { player, args ->
        val npc: Npc by args
        val rotationType: NpcRotationType by args

        npc.addProperty(
            NpcProperty(
                NpcProperty.Internal.ROTATION_TYPE,
                rotationType == NpcRotationType.PER_PLAYER,
                NpcPropertyType.Types.BOOLEAN_TYPE
            )
        )

        val prevLocation = npc.getLocation()

        if (rotationType == NpcRotationType.FIXED) {
            npc.addProperty(
                NpcProperty(
                    NpcProperty.Internal.LOCATION,
                    Location(
                        prevLocation.world,
                        prevLocation.x,
                        prevLocation.y,
                        prevLocation.z,
                        player.yaw,
                        player.pitch
                    ),
                    NpcPropertyType.Types.LOCATION_TYPE
                )
            )
        }

        npc.refresh()

        player.sendText {
            appendSuccessPrefix()
            success("Die Rotation des Npc ")
            variableValue(npc.uniqueName)
            success(" wurde auf $rotationType geändert.")
        }
    }
}