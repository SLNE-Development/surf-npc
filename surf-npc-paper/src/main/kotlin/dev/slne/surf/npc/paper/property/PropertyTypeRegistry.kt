package dev.slne.surf.npc.paper.property

import dev.slne.surf.api.core.util.mutableObjectSetOf
import dev.slne.surf.api.core.util.toObjectSet
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
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

