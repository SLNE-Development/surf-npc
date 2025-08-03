package dev.slne.surf.npc.bukkit.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.readableString
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.clickRunsCommand
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration

class NpcInfoCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_INFO)
        npcArgument("npc")
        playerExecutor { player, args ->
            val npc: Npc by args

            val displayName = npc.getPropertyValue(NpcProperty.Internal.DISPLAYNAME, Component::class) ?: error("NPC ${npc.uniqueName} has no display name set")
            val location = npc.getPropertyValue(NpcProperty.Internal.LOCATION, NpcLocation::class) ?: error("NPC ${npc.uniqueName} has no location set")

            val rotationType = npc.getPropertyValue(NpcProperty.Internal.ROTATION_TYPE, Boolean::class) ?: error("NPC ${npc.uniqueName} has no rotation type set")
            val skinOwner = npc.getPropertyValue(NpcProperty.Internal.SKIN_OWNER, String::class) ?: error("NPC ${npc.uniqueName} has no skin owner set")
            val global = npc.getPropertyValue(NpcProperty.Internal.VISIBILITY_GLOBAL, Boolean::class) ?: error("NPC ${npc.uniqueName} has no global visibility set")

            player.sendText {
                append {
                    info("Npc Informationen".toSmallCaps())
                    decorate(TextDecoration.BOLD)
                }
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Name: ")
                variableValue(npc.uniqueName)
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Anzeigename: ")
                append(displayName)
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("ID: ")
                variableValue(npc.id)
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Nametag-ID: ")
                variableValue(npc.nameTagId)
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Uuid: ")
                variableValue(npc.npcUuid.toString())
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                append {
                    variableKey("Ort: ")
                    variableValue(location.readableString())
                    clickRunsCommand("/npc teleport ${npc.id}")
                }
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Rotation: ")
                variableValue(if (rotationType) "Per-Player" else "Fixed")
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Skin: ")
                variableValue(skinOwner)
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Global: ")
                variableValue(if (global) "Ja" else "Nein")
            }
        }
    }
}