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
class NpcHideEvent(
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