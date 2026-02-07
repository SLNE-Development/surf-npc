package dev.slne.surf.npc.bukkit.listener

import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.bukkit.controller.npcController
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent

class WorldChangeListener : Listener {
    @EventHandler
    fun onWorldChanged(event: PlayerChangedWorldEvent) {
        val player = event.player
        val world = player.world

        npcController.npcs.mapNotNull {
            val npcLocation = it.getPropertyValue(NpcProperty.Internal.LOCATION, Location::class)
            if (npcLocation?.world?.name == world.name) it to npcLocation else null
        }.forEach { (npc, _) ->
            npcController.refreshNpc(npc, player.uniqueId)
        }
    }
}