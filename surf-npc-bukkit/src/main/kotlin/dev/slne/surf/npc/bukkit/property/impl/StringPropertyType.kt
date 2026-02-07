package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.property.NpcPropertyType

class StringPropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is String) { "Expected String, got ${value::class}" }
        return value
    }

    override fun decode(value: String): String {
        return value
    }
}


