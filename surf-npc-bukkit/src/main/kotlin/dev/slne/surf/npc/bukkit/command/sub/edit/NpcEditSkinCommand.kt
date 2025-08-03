package dev.slne.surf.npc.bukkit.command.sub.edit

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.property.BukkitNpcProperty
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.skinDataFromName
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcEditSkinCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_EDIT_SKIN)
        npcArgument("npc")
        stringArgument("skinPlayer")
        playerExecutor { player, args ->
            val npc: Npc by args
            val skinPlayer: String by args

            player.sendText {
                appendPrefix()
                info("Die Skin-Daten für den Spieler ")
                variableValue(skinPlayer)
                info(" werden geladen...")
            }

            plugin.launch {
                val skinData = skinDataFromName(skinPlayer)


                npc.addProperty(BukkitNpcProperty(
                    NpcProperty.Internal.SKIN_OWNER,
                    skinData.ownerName,
                    propertyTypeRegistry.get(NpcPropertyType.Types.STRING) ?: return@launch
                ))
                npc.addProperty(BukkitNpcProperty(
                    NpcProperty.Internal.SKIN_TEXTURE,
                    skinData.value,
                    propertyTypeRegistry.get(NpcPropertyType.Types.STRING) ?: return@launch
                ))
                npc.addProperty(BukkitNpcProperty(
                    NpcProperty.Internal.SKIN_SIGNATURE,
                    skinData.signature,
                    propertyTypeRegistry.get(NpcPropertyType.Types.STRING) ?: return@launch
                ))
                npc.refresh()

                player.sendText {
                    appendPrefix()
                    success("Die Skin-Daten für den Npc ")
                    variableValue(npc.uniqueName)
                    success(" wurden aktualisiert.")
                }
            }
        }
    }
}