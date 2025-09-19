package dev.slne.surf.npc.api.util

import dev.slne.surf.npc.api.event.NpcEvent
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.NpcEventHandler

inline fun <reified T : NpcEvent> Npc.addEventHandler(noinline handler: NpcEventHandler<T>) {
    this.addEventHandler(T::class, handler)
}
