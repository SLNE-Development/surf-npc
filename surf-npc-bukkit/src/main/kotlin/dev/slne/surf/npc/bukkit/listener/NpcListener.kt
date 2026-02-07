package dev.slne.surf.npc.bukkit.listener

import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.protocol.player.InteractionHand
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity
import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.npc.api.event.NpcCollisionEvent
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.bukkit.controller.npcController
import dev.slne.surf.npc.bukkit.plugin
import org.bukkit.Location
import org.bukkit.entity.Player

class NpcListener : PacketListener {
    override fun onPacketReceive(event: PacketReceiveEvent) {
        val player = event.getPlayer<Player>() ?: return

        when (event.packetType) {
            PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION -> {
                for (npc in npcController.getNpcs()) {
                    val npcLoc =
                        npc.getPropertyValue(NpcProperty.Internal.LOCATION, Location::class)
                            ?: continue
                    val playerLoc = player.location

                    if (playerLoc.world.name != npcLoc.world.name) {
                        continue
                    }

                    if (playerLoc.distanceSquared(npcLoc) > 20 * 20) {
                        continue
                    }

                    npc.refreshRotation(player.uniqueId)
                }
            }

            PacketType.Play.Client.PLAYER_POSITION -> {
                for (npc in npcController.getNpcs()) {
                    val npcLoc =
                        npc.getPropertyValue(NpcProperty.Internal.LOCATION, Location::class)
                            ?: continue
                    val playerLoc = player.location

                    if (playerLoc.world.name != npcLoc.world.name) {
                        continue
                    }

                    if (playerLoc.distanceSquared(npcLoc) > 1 * 1) {
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
    }
}