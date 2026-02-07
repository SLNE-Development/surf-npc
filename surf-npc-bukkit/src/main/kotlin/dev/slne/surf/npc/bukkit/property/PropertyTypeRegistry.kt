package dev.slne.surf.npc.bukkit.property

import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet

val propertyTypeRegistry = PropertyTypeRegistry()

class PropertyTypeRegistry {
    val types = mutableObjectSetOf<NpcPropertyType>()

    fun register(type: NpcPropertyType) {
        val existing = types.firstOrNull { it.id == type.id }

        if (existing != null) {
            types.remove(existing)
        }

        types.add(type)
    }

    fun unregister(type: NpcPropertyType) {
        types.remove(type)
    }

    fun get(id: String): NpcPropertyType? {
        return types.firstOrNull { it.id == id }
    }

    fun getIds(): ObjectSet<String> {
        return types.map { it.id }.toObjectSet()
    }
}

