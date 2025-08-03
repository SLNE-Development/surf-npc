package dev.slne.surf.npc.bukkit.listener

import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity
import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.npc.api.event.NpcCollisionEvent
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.util.toLocation
import dev.slne.surf.npc.core.controller.npcController
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class NpcListener : PacketListener {
    override fun onPacketReceive(event: PacketReceiveEvent) {
        val player = event.getPlayer<Player>() ?: return

        when(event.packetType) {
            PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION -> {
                for (npc in npcController.getNpcs()) {
                    val npcLoc = npc.getPropertyValue(NpcProperty.Internal.LOCATION, NpcLocation::class) ?: continue
                    val playerLoc = player.location

                    if (playerLoc.distanceSquared(npcLoc.toLocation()) > 20 * 20) {
                        continue
                    }

                    npc.refreshRotation(player.uniqueId)
                }
            }
            PacketType.Play.Client.PLAYER_POSITION -> {
                for (npc in npcController.getNpcs()) {
                    val npcLoc = npc.getPropertyValue(NpcProperty.Internal.LOCATION, NpcLocation::class) ?: continue
                    val playerLoc = player.location

                    if (playerLoc.distanceSquared(npcLoc.toLocation()) > 1 * 1) {
                        continue
                    }

                    plugin.launch(plugin.entityDispatcher(player)) {
                        NpcCollisionEvent(
                            npc,
                            player
                        ).callEvent()
                    }
                }
            }
            PacketType.Play.Client.INTERACT_ENTITY -> {
                val packet = WrapperPlayClientInteractEntity(event)
                val npc = npcController.getNpc(packet.entityId) ?: return

                if(packet.action != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                    return
                }

                plugin.launch(plugin.entityDispatcher(player)) {
                    NpcInteractEvent(
                        npc,
                        player
                    ).callEvent()
                }
            }
        }
    }
}