package dev.slne.surf.npc.paper.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.CommonComponents
import dev.slne.surf.api.core.messages.adventure.clickRunsCommand
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.paper.command.argument.npcArgument
import dev.slne.surf.npc.paper.util.PermissionRegistry
import dev.slne.surf.npc.paper.util.readableString
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Location

fun CommandAPICommand.npcInfoCommand() = subcommand("info") {
    withPermission(PermissionRegistry.COMMAND_NPC_INFO)
    npcArgument("npc")
    playerExecutor { player, args ->
        val npc: Npc by args

        val displayName =
            npc.getPropertyValue(NpcProperty.Internal.DISPLAYNAME, Component::class)
                ?: error("NPC ${npc.uniqueName} has no display name set")
        val location = npc.getPropertyValue(NpcProperty.Internal.LOCATION, Location::class)
            ?: error("NPC ${npc.uniqueName} has no location set")

        val rotationType =
            npc.getPropertyValue(NpcProperty.Internal.ROTATION_TYPE, NpcRotationType::class)
                ?: error("NPC ${npc.uniqueName} has no rotation type set")
        val skinData = npc.getPropertyValue(NpcProperty.Internal.SKIN_DATA, NpcSkin::class)
            ?: error("NPC ${npc.uniqueName} has no skin data set")

        player.sendText {
            info("Npc Informationen".toSmallCaps(), TextDecoration.BOLD)
            appendNewline()

            append(CommonComponents.EM_DASH)
            appendSpace()
            variableKey("Name: ")
            variableValue(npc.uniqueName)
            appendNewline()

            append(CommonComponents.EM_DASH)
            appendSpace()
            variableKey("Anzeigename: ")
            append(displayName)
            appendNewline()

            append(CommonComponents.EM_DASH)
            appendSpace()
            variableKey("ID: ")
            variableValue(npc.id)
            appendNewline()

            append(CommonComponents.EM_DASH)
            appendSpace()
            variableKey("Nametag-ID: ")
            variableValue(npc.nameTagId)
            appendNewline()

            append(CommonComponents.EM_DASH)
            appendSpace()
            variableKey("Uuid: ")
            variableValue(npc.npcUuid.toString())
            appendNewline()

            append(CommonComponents.EM_DASH)
            appendSpace()
            append {
                variableKey("Ort: ")
                variableValue(location.readableString())
                clickRunsCommand("/npc teleport ${npc.id}")
            }
            appendNewline()

            append(CommonComponents.EM_DASH)
            appendSpace()
            variableKey("Rotation: ")
            variableValue(rotationType.name)
            appendNewline()

            append(CommonComponents.EM_DASH)
            appendSpace()
            variableKey("Skin: ")
            variableValue(skinData.ownerName)
            appendNewline()
        }
    }
}