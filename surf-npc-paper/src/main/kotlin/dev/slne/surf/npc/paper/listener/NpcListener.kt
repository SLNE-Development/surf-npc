package dev.slne.surf.npc.paper.listener

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.manager.server.ServerVersion
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.protocol.player.InteractionHand
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientAttack
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity
import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.npc.api.event.NpcCollisionEvent
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.paper.controller.npcController
import dev.slne.surf.npc.paper.plugin
import org.bukkit.entity.Player

class NpcListener : PacketListener {
    override fun onPacketReceive(event: PacketReceiveEvent) {
        val player = event.getPlayer<Player>() ?: return

        when (event.packetType) {
            PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION -> {
                for (npc in npcController.npcs) {
                    val npcLoc = npc.getLocation()
                    val npcWorld = npcLoc.world ?: continue

                    val playerLoc = player.location
                    val playerWorld = playerLoc.world ?: continue

                    if (playerWorld.name != npcWorld.name) {
                        continue
                    }

                    if (!npc.rotationBox.contains(player.location.toVector())) {
                        continue
                    }

                    npc.refreshRotation()
                }
            }

            PacketType.Play.Client.PLAYER_POSITION -> {
                for (npc in npcController.npcs) {
                    val npcLoc = npc.getLocation()
                    val npcWorld = npcLoc.world ?: continue
                    val playerLoc = player.location
                    val playerWorld = playerLoc.world ?: continue

                    if (playerWorld.name != npcWorld.name) {
                        continue
                    }

                    if (!npc.boundingBox.contains(player.location.toVector())) {
                        continue
                    }

                    plugin.launch(plugin.entityDispatcher(player)) {
                        NpcCollisionEvent(
                            player,
                            npc
                        ).callEvent()
                    }
                }
            }

            PacketType.Play.Client.INTERACT_ENTITY -> {
                val packet = WrapperPlayClientInteractEntity(event)
                val npc = npcController.getNpc(packet.entityId) ?: return

                if (PacketEvents.getAPI().serverManager.version.isNewerThanOrEquals(ServerVersion.V_26_1)) {
                    when (packet.action) {
                        WrapperPlayClientInteractEntity.InteractAction.INTERACT_AT -> {
                            if (packet.hand == InteractionHand.OFF_HAND) {
                                plugin.launch(plugin.entityDispatcher(player)) {
                                    NpcInteractEvent(
                                        player,
                                        npc
                                    ).callEvent()
                                }
                            }
                        }

                        else -> Unit
                    }
                } else {
                    when (packet.action) {
                        WrapperPlayClientInteractEntity.InteractAction.ATTACK -> {
                            plugin.launch(plugin.entityDispatcher(player)) {
                                NpcInteractEvent(
                                    player,
                                    npc
                                ).callEvent()
                            }
                        }

                        WrapperPlayClientInteractEntity.InteractAction.INTERACT -> {
                            if (packet.hand != InteractionHand.MAIN_HAND) {
                                return
                            }

                            plugin.launch(plugin.entityDispatcher(player)) {
                                NpcInteractEvent(
                                    player,
                                    npc
                                ).callEvent()
                            }
                        }

                        WrapperPlayClientInteractEntity.InteractAction.INTERACT_AT -> {
                            // This is already handled by INTERACT action, so we can ignore it.
                        }
                    }
                }
            }

            PacketType.Play.Client.ATTACK -> {
                val packet = WrapperPlayClientAttack(event)
                val npc = npcController.getNpc(packet.entityId) ?: return

                plugin.launch(plugin.entityDispatcher(player)) {
                    NpcInteractEvent(
                        player,
                        npc
                    ).callEvent()
                }
            }
        }
    }
}