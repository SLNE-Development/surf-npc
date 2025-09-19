package dev.slne.surf.npc.bukkit.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.NpcCreatorType
import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.readableString
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.CommonComponents
import dev.slne.surf.surfapi.core.api.messages.adventure.clickRunsCommand
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration

fun CommandAPICommand.npcInfoCommand() = subcommand("info") {
    withPermission(PermissionRegistry.COMMAND_NPC_INFO)
    npcArgument("npc")
    playerExecutor { player, args ->
        val npc: Npc by args

        val displayName =
            npc.getPropertyValue(NpcProperty.Internal.DISPLAYNAME, Component::class)
                ?: error("NPC ${npc.uniqueName} has no display name set")
        val location = npc.getPropertyValue(NpcProperty.Internal.LOCATION, NpcLocation::class)
            ?: error("NPC ${npc.uniqueName} has no location set")

        val rotationType =
            npc.getPropertyValue(NpcProperty.Internal.ROTATION_TYPE, Boolean::class)
                ?: error("NPC ${npc.uniqueName} has no rotation type set")
        val skinData = npc.getPropertyValue(NpcProperty.Internal.SKIN_DATA, NpcSkin::class)
            ?: error("NPC ${npc.uniqueName} has no skin data set")
        val npcCreator =
            npc.getPropertyValue(NpcProperty.Internal.CREATOR_TYPE, NpcCreatorType::class)

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
            variableKey("Ersteller: ")
            variableValue(npcCreator?.name() ?: "Unbekannt")
            appendNewline()

            append(CommonComponents.EM_DASH)
            appendSpace()
            variableKey("Rotation: ")
            variableValue(if (rotationType) "Per-Player" else "Fixed")
            appendNewline()

            append(CommonComponents.EM_DASH)
            appendSpace()
            variableKey("Skin: ")
            variableValue(skinData.ownerName)
            appendNewline()
        }
    }
}