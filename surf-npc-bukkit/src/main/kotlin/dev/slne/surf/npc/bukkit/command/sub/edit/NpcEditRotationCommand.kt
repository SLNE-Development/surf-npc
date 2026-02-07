package dev.slne.surf.npc.bukkit.command.sub.edit

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.command.argument.rotationTypeArgument
import dev.slne.surf.npc.bukkit.npc.rotation.BukkitNpcRotation
import dev.slne.surf.npc.bukkit.property.BukkitNpcProperty
import dev.slne.surf.npc.bukkit.property.propertyTypeRegistry
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.npcEditRotationCommand() = subcommand("rotation") {
    withPermission(PermissionRegistry.COMMAND_NPC_EDIT_ROTATION)
    npcArgument("npc")
    rotationTypeArgument("rotationType")
    playerExecutor { player, args ->
        val npc: Npc by args
        val rotationType: NpcRotationType by args

        npc.addProperty(
            BukkitNpcProperty(
                NpcProperty.Internal.ROTATION_TYPE,
                rotationType == NpcRotationType.PER_PLAYER,
                propertyTypeRegistry.get(NpcPropertyType.Types.BOOLEAN_ID) ?: return@playerExecutor
            )
        )

        if (rotationType == NpcRotationType.FIXED) {
            npc.addProperty(
                BukkitNpcProperty(
                    NpcProperty.Internal.R,
                    BukkitNpcRotation(
                        player.yaw, player.pitch
                    ),
                    propertyTypeRegistry.get(NpcPropertyType.Types.NPC_ROTATION_ID)
                        ?: return@playerExecutor
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