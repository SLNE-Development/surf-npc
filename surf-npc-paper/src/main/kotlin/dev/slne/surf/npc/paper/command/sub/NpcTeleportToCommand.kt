package dev.slne.surf.npc.paper.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.entitySelectorArgumentOnePlayer
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.paper.command.argument.npcArgument
import dev.slne.surf.npc.paper.util.PermissionRegistry
import org.bukkit.Location
import org.bukkit.entity.Player

fun CommandAPICommand.npcTeleportToCommand() = subcommand("teleportto") {
    withPermission(PermissionRegistry.COMMAND_NPC_TELEPORT_TO)
    npcArgument("npc")
    entitySelectorArgumentOnePlayer("target", true)
    playerExecutor { player, args ->
        val npc: Npc by args
        val target: Player? by args

        val location = npc.getPropertyValue(NpcProperty.Internal.LOCATION, Location::class)
            ?: return@playerExecutor run {
                player.sendText {
                    appendErrorPrefix()
                    error("Der NPC hat keine gültige Position.")
                }
            }

        if (target != null) {
            val targetPlayer = target ?: return@playerExecutor run {
                player.sendText {
                    appendErrorPrefix()
                    error("Der angegebene Spieler ist nicht mehr online.")
                }
            }

            player.sendText {
                appendInfoPrefix()
                info("${targetPlayer.name} wird zu ${npc.uniqueName} teleportiert...")
            }

            targetPlayer.teleportAsync(location).thenRun {
                player.sendText {
                    appendSuccessPrefix()
                    success("${targetPlayer.name} wurde zu ${npc.uniqueName} teleportiert.")
                }
            }
            return@playerExecutor
        }

        player.sendText {
            appendInfoPrefix()
            info("Du wirst zu ${npc.uniqueName} teleportiert...")
        }

        player.teleportAsync(location).thenRun {
            player.sendText {
                appendSuccessPrefix()
                success("Du wurdest zu ${npc.uniqueName} teleportiert.")
            }
        }
        return@playerExecutor
    }
}