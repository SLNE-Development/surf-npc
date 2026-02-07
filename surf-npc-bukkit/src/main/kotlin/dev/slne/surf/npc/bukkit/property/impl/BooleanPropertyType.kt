package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.property.NpcPropertyType

class BooleanPropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is Boolean) { "Expected Boolean, got ${value::class}" }
        return value.toString()
    }

    override fun decode(value: String): Boolean {
        return value.toBooleanStrict()
    }
}


