package dev.slne.surf.npc.paper.listener

import dev.slne.surf.npc.paper.controller.npcController
import io.canvasmc.canvas.event.EntityPostTeleportAsyncEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object FoliaAdditionsListener : Listener {
    @EventHandler
    fun onPostTeleport(event: EntityPostTeleportAsyncEvent) {
        val player = event.entity as? Player ?: return
        val world = player.world

        npcController.npcs.mapNotNull {
            val npcLocation = it.getLocation()
            if (npcLocation.world?.name == world.name) it to npcLocation else null
        }.forEach { (npc, _) ->
            npcController.refreshNpc(npc, player.uniqueId)
        }
    }
}