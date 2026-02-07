package dev.slne.surf.npc.paper.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.entitySelectorArgumentOnePlayer
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.paper.command.argument.npcArgument
import dev.slne.surf.npc.paper.util.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.entity.Player

fun CommandAPICommand.npcTeleportHereCommand() = subcommand("teleporthere") {
    withPermission(PermissionRegistry.COMMAND_NPC_TELEPORT_HERE)
    npcArgument("npc")
    entitySelectorArgumentOnePlayer("target", true)
    playerExecutor { player, args ->
        val npc: Npc by args
        val target: Player? by args

        if (target != null) {
            val targetPlayer = target ?: return@playerExecutor run {
                player.sendText {
                    appendErrorPrefix()
                    error("Der angegebene Spieler ist nicht mehr online.")
                }
            }

            npc.teleport(targetPlayer)
            player.sendText {
                appendSuccessPrefix()
                success("Der NPC ${npc.uniqueName} wurde zu ${targetPlayer.name} teleportiert.")
            }
            return@playerExecutor
        }

        npc.teleport(player)
        player.sendText {
            appendSuccessPrefix()
            success("Der NPC ${npc.uniqueName} wurde zu dir teleportiert.")
        }
        return@playerExecutor
    }
}