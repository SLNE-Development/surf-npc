package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.property.NpcPropertyType

class LongPropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is Long) { "Expected Long, got ${value::class}" }
        return value.toString()
    }

    override fun decode(value: String): Long {
        return value.toLong()
    }
}


