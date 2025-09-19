package dev.slne.surf.npc.api.event

import dev.slne.surf.npc.api.npc.Npc
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Event triggered when an NPC is spawned.
 *
 * @property npc The NPC that was spawned.
 * @property player The player associated with the spawn event, if applicable.
 */
class NpcShowEvent(
    override val player: Player,
    override val npc: Npc
) : Event(), NpcPlayerEvent {

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