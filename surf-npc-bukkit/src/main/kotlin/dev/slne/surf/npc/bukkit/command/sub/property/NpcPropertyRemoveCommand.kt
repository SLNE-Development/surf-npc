package dev.slne.surf.npc.bukkit.command.sub.property

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.subcommand

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.npcPropertyRemoveCommand() = subcommand("remove") {
    withPermission(PermissionRegistry.COMMAND_NPC_PROPERTY_REMOVE)
    npcArgument("npc")
    stringArgument("key")
    playerExecutor { player, args ->
        val npc: Npc by args
        val key: String by args

        if (npc.isFromPlugin()) {
            player.sendText {
                appendErrorPrefix()
                error("Der Npc wurde von einem Plugin erstellt und kann daher nicht bearbeitet werden.")
            }
            return@playerExecutor
        }

        if (!npc.hasProperty(key)) {
            player.sendText {
                appendErrorPrefix()
                error("Der NPC '${npc.uniqueName}' besitzt keine Property mit dem Key '${key}'.")
            }
            return@playerExecutor
        }

        npc.removeProperty(key)

        player.sendText {
            appendSuccessPrefix()
            success("Die Property '${key}' wurde erfolgreich vom NPC '${npc.uniqueName}' entfernt.")
        }
    }
}