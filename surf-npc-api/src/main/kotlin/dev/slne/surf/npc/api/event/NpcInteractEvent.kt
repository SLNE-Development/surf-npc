package dev.slne.surf.npc.api.event

import dev.slne.surf.npc.api.npc.Npc
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Event triggered when a player interacts with an NPC.
 *
 * @property npc The NPC that was interacted with.
 * @property player The player who interacted with the NPC.
 */
class NpcInteractEvent(
    val npc: Npc,
    val player: Player
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