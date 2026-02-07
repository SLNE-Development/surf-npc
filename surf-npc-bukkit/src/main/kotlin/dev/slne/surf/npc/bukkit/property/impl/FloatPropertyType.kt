package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.property.NpcPropertyType

class FloatPropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is Float) { "Expected Float, got ${value::class}" }
        return value.toString()
    }

    override fun decode(value: String): Float {
        return value.toFloat()
    }
}


