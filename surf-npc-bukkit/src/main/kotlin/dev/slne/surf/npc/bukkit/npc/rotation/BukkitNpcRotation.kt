package dev.slne.surf.npc.bukkit.rotation

import dev.slne.surf.npc.api.npc.rotation.NpcRotation

data class BukkitNpcRotation (
    override val yaw: Float,
    override val pitch: Float
) : NpcRotation