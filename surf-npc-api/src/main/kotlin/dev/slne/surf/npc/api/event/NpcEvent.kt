package dev.slne.surf.npc.api.event

import dev.slne.surf.npc.api.npc.Npc

interface NpcEvent {
    val npc: Npc
}