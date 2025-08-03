package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import net.kyori.adventure.text.format.NamedTextColor

class NamedTextColorPropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is NamedTextColor) { "Expected NamedTextColor, got ${value::class}" }
        return value.value().toString()
    }

    override fun decode(value: String): NamedTextColor {
        return NamedTextColor.namedColor(value.toInt()) ?: error("Invalid NamedTextColor value: $value")
    }
}


