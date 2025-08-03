package dev.slne.surf.npc.api.event

import dev.slne.surf.npc.api.npc.Npc
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Event triggered when an NPC despawns.
 *
 * @property npc The NPC that despawned.
 * @property player The player associated with the despawn event, if applicable.
 */
class NpcDespawnEvent (
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