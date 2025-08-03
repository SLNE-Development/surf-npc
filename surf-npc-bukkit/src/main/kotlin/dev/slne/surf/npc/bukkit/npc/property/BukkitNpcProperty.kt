package dev.slne.surf.npc.bukkit.property

import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType

data class BukkitNpcProperty(
    override val key: String,
    override val value: Any,
    override val type: NpcPropertyType
) : NpcProperty
