package dev.slne.surf.npc.bukkit.command.sub.edit

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.command.argument.rotationTypeArgument
import dev.slne.surf.npc.bukkit.property.BukkitNpcProperty
import dev.slne.surf.npc.bukkit.rotation.BukkitNpcRotation
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcEditRotationCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_EDIT_ROTATION)
        npcArgument("npc")
        rotationTypeArgument("rotationType")
        playerExecutor { player, args ->
            val npc: Npc by args
            val rotationType: NpcRotationType by args

            npc.addProperty(BukkitNpcProperty(
                NpcProperty.Internal.ROTATION_TYPE,
                rotationType == NpcRotationType.PER_PLAYER,
                propertyTypeRegistry.get(NpcPropertyType.Types.BOOLEAN) ?: return@playerExecutor
            ))

            if(rotationType == NpcRotationType.FIXED) {
                npc.addProperty(BukkitNpcProperty(
                    NpcProperty.Internal.ROTATION_FIXED,
                    BukkitNpcRotation(
                        player.yaw, player.pitch
                    ),
                    propertyTypeRegistry.get(NpcPropertyType.Types.NPC_ROTATION) ?: return@playerExecutor
                ))
            }

            npc.refresh()

            player.sendText {
                appendPrefix()
                success("Die Rotation des Npc ")
                variableValue(npc.uniqueName)
                success(" wurde auf $rotationType geändert.")
            }
        }
    }
}