package dev.slne.surf.npc.paper.property.impl

import dev.slne.surf.npc.api.npc.NpcPose
import dev.slne.surf.npc.api.npc.property.NpcPropertyType

class NpcPosePropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is NpcPose) { "Expected NpcPose, got ${value::class}" }
        return value.toString()
    }

    override fun decode(value: String): NpcPose {
        return NpcPose.valueOf(value)
    }
}


