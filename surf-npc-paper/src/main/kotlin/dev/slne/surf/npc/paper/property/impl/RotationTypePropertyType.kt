package dev.slne.surf.npc.paper.property.impl

import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType

class RotationTypePropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is NpcRotationType) { "Expected NpcRotationType, got ${value::class}" }
        return value.toString()
    }

    override fun decode(value: String): NpcRotationType {
        return NpcRotationType.valueOf(value)
    }
}


