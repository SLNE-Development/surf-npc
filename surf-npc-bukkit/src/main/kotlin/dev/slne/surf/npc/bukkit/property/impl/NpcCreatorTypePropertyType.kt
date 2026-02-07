package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.NpcCreatorType
import dev.slne.surf.npc.api.npc.property.NpcPropertyType

class NpcCreatorTypePropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is NpcCreatorType) { "Expected NpcCreatorType, got ${value::class}" }
        return "${if (value.isClient()) "player" else "plugin"}:${value.name()}"
    }

    override fun decode(value: String): NpcCreatorType {
        val parts = value.split(":")
        require(parts.size == 2) { "Invalid creator type format: $value" }

        return when (parts[0]) {
            "player" -> NpcCreatorType.Client(parts[1])
            "plugin" -> NpcCreatorType.Plugin(parts[1])
            else -> throw IllegalArgumentException("Unknown creator type: ${parts[0]}")
        }
    }
}


