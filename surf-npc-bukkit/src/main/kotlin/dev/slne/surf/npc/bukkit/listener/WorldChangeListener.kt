package dev.slne.surf.npc.bukkit.listener

import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.core.controller.npcController
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent

class WorldChangeListener : Listener {
    @EventHandler
    fun onWorldChanged(event: PlayerChangedWorldEvent) {
        val player = event.player
        val world = player.world

        npcController.getNpcs().forEach {
            val npcLocation = it.getPropertyValue(NpcProperty.Internal.LOCATION, NpcLocation::class)
                ?: return@forEach
            val npcWorldName = npcLocation.world

            if (npcWorldName == world.name) {
                npcController.reShowNpc(it, player.uniqueId)
            }
        }
    }
}