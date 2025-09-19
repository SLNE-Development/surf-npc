package dev.slne.surf.npc.bukkit.listener

import dev.slne.surf.npc.api.event.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class InternalNpcEventListener : Listener {
    @EventHandler
    fun onNpcCollision(event: NpcCollisionEvent) {
        event.npc.callHandlers(event)
    }

    @EventHandler
    fun onNpcCreate(event: NpcCreateEvent) {
        event.npc.callHandlers(event)
    }

    @EventHandler
    fun onNpcDelete(event: NpcDeleteEvent) {
        event.npc.callHandlers(event)
    }

    @EventHandler
    fun onNpcHide(event: NpcHideEvent) {
        event.npc.callHandlers(event)
    }

    @EventHandler
    fun onNpcInteract(event: NpcInteractEvent) {
        event.npc.callHandlers(event)
    }

    @EventHandler
    fun onNpcShow(event: NpcShowEvent) {
        event.npc.callHandlers(event)
    }
}