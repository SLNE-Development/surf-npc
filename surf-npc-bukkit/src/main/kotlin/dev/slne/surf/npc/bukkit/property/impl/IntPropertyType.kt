package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.property.NpcPropertyType

class IntPropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is Int) { "Expected Int, got ${value::class}" }
        return value.toString()
    }

    override fun decode(value: String): Int {
        return value.toInt()
    }
}


