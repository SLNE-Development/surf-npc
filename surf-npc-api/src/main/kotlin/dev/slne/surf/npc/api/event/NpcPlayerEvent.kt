package dev.slne.surf.npc.api.event

import org.bukkit.entity.Player

interface NpcPlayerEvent : NpcEvent {
    val player: Player
}