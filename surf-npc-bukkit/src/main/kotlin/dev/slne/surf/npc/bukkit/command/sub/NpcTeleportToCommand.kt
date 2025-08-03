package dev.slne.surf.npc.bukkit.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.toLocation
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.entity.Player

class NpcTeleportToCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_TELEPORT_TO)
        npcArgument("npc")
        playerArgument("target", true)
        playerExecutor { player, args ->
            val npc: Npc by args
            val target: Player? by args

            val location = npc.getPropertyValue(NpcProperty.Internal.LOCATION, NpcLocation::class) ?: return@playerExecutor run {
                    player.sendText {
                        appendPrefix()
                        error("Der NPC hat keine gültige Position.")
                    }
                }

            val bukkitLocation = location.toLocation()

            if (target != null) {
                val targetPlayer = target ?: return@playerExecutor run {
                    player.sendText {
                        appendPrefix()
                        error("Der angegebene Spieler ist nicht mehr online.")
                    }
                }

                player.sendText {
                    appendPrefix()
                    info("${targetPlayer.name} wird zu ${npc.uniqueName} teleportiert...")
                }

                targetPlayer.teleportAsync(bukkitLocation).thenRun {
                    player.sendText {
                        appendPrefix()
                        success("${targetPlayer.name} wurde zu ${npc.uniqueName} teleportiert.")
                    }
                }
                return@playerExecutor
            }

            player.sendText {
                appendPrefix()
                info("Du wirst zu ${npc.uniqueName} teleportiert...")
            }

            player.teleportAsync(bukkitLocation).thenRun {
                player.sendText {
                    appendPrefix()
                    success("Du wurdest zu ${npc.uniqueName} teleportiert.")
                }
            }
            return@playerExecutor
        }
    }
}