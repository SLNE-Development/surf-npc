package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.bukkit.util.miniMessage
import net.kyori.adventure.text.Component

class ComponentPropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is Component) { "Expected Component, got ${value::class}" }
        return miniMessage.serialize(value)
    }

    override fun decode(value: String): Component {
        return miniMessage.deserialize(value)
    }
}


