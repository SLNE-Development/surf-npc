package dev.slne.surf.npc.bukkit.npc.property.impl

import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import java.util.*

class UuidPropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is UUID) { "Expected Uuid, got ${value::class}" }
        return value.toString()
    }

    override fun decode(value: String): UUID {
        return UUID.fromString(value)
    }
}


