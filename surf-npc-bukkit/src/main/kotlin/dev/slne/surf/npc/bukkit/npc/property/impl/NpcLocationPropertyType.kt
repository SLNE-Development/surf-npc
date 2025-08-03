package dev.slne.surf.npc.bukkit.npc.property.impl

import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.bukkit.npc.location.BukkitNpcLocation

class NpcLocationPropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is NpcLocation) { "Expected SNpcLocation, got ${value::class}" }
        return "${value.world}:${value.x}:${value.y}:${value.z}"
    }

    override fun decode(value: String): NpcLocation {
        val parts = value.split(":")
        require(parts.size == 4) { "Invalid location format: $value" }

        val worldName = parts[0]
        val x = parts[1].toDoubleOrNull() ?: throw IllegalArgumentException("Invalid X coordinate: ${parts[1]}")
        val y = parts[2].toDoubleOrNull() ?: throw IllegalArgumentException("Invalid Y coordinate: ${parts[2]}")
        val z = parts[3].toDoubleOrNull() ?: throw IllegalArgumentException("Invalid Z coordinate: ${parts[3]}")

        return BukkitNpcLocation(x, y, z, worldName)
    }
}


