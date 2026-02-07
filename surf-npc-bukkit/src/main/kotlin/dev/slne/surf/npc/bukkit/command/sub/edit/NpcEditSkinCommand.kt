package dev.slne.surf.npc.bukkit.command.sub.edit

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.skinDataFromName
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.npcEditSkinCommand() = subcommand("skin") {
    withPermission(PermissionRegistry.COMMAND_NPC_EDIT_SKIN)
    npcArgument("npc")
    stringArgument("skinPlayer")
    playerExecutor { player, args ->
        val npc: Npc by args
        val skinPlayer: String by args

        player.sendText {
            appendInfoPrefix()
            info("Die Skin-Daten für den Spieler ")
            variableValue(skinPlayer)
            info(" werden geladen...")
        }

        plugin.launch {
            val skinData = skinDataFromName(skinPlayer)


            npc.addProperty(
                NpcProperty(
                    NpcProperty.Internal.SKIN_DATA,
                    skinData,
                    NpcPropertyType.Types.SKIN_DATA_TYPE
                )
            )
            npc.refresh()

            player.sendText {
                appendSuccessPrefix()
                success("Die Skin-Daten für den Npc ")
                variableValue(npc.uniqueName)
                success(" wurden aktualisiert.")
            }
        }
    }
}