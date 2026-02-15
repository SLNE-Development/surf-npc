package dev.slne.surf.npc.api.util

import dev.slne.surf.npc.api.event.NpcEvent
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.NpcEventHandler

/**
 * Registers an event handler for a specific type of NPC event.
 *
 * @param T The type of the event that the handler will process, which must extend [NpcEvent].
 * @param handler The handler function to execute when the specified event occurs.
 */
inline fun <reified T : NpcEvent> Npc.addEventHandler(noinline handler: NpcEventHandler<T>) {
    this.addEventHandler(T::class, handler)
}

