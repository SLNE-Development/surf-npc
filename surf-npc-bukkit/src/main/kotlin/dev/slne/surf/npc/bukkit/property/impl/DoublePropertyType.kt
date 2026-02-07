package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.property.NpcPropertyType

class DoublePropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is Double) { "Expected Double, got ${value::class}" }
        return value.toString()
    }

    override fun decode(value: String): Double {
        return value.toDouble()
    }
}


