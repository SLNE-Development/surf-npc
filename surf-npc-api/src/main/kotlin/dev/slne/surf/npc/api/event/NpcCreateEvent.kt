package dev.slne.surf.npc.api.event

import dev.slne.surf.npc.api.npc.Npc
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Event triggered when an NPC is created.
 *
 * @property npc The NPC that was created.
 */
class NpcCreateEvent (
    val npc: Npc
) : Event() {

    /**
     * Returns the handler list for this event.
     *
     * @return The handler list.
     */
    override fun getHandlers(): HandlerList = handlerList

    companion object {
        /**
         * The static handler list for this event.
         */
        @JvmStatic
        val handlerList = HandlerList()
    }
}