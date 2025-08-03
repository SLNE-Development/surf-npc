package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotation
import dev.slne.surf.npc.bukkit.rotation.BukkitNpcRotation

class NpcRotationPropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is NpcRotation) { "Expected SNpcRotation, got ${value::class}" }
        return "${value.yaw}:${value.pitch}"
    }

    override fun decode(value: String): NpcRotation {
        return value.split(":").let {
            require(it.size == 2) { "Invalid rotation format: $value" }

            val yaw = it[0].toFloatOrNull() ?: throw IllegalArgumentException("Invalid yaw value: ${it[0]}")
            val pitch = it[1].toFloatOrNull() ?: throw IllegalArgumentException("Invalid pitch value: ${it[1]}")

            BukkitNpcRotation(yaw, pitch)
        }
    }
}


