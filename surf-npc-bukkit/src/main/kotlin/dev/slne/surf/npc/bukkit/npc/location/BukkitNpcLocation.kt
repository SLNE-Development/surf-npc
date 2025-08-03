package dev.slne.surf.npc.bukkit.npc.location

import dev.slne.surf.npc.api.npc.location.NpcLocation

data class BukkitNpcLocation (
    override val x: Double,
    override val y: Double,
    override val z: Double,
    override val world: String
) : NpcLocation