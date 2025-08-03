package dev.slne.surf.npc.api.event

import dev.slne.surf.npc.api.npc.Npc
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Event triggered when a player collides with an NPC.
 *
 * @property npc The NPC involved in the collision.
 * @property player The player who collided with the NPC.
 */
class NpcCollisionEvent (
    val npc: Npc,
    val player: Player
): Event() {

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