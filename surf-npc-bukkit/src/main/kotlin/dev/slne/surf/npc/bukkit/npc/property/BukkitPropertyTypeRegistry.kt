package dev.slne.surf.npc.bukkit.property

import com.google.auto.service.AutoService
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.core.property.PropertyTypeRegistry
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.util.Services

@AutoService(PropertyTypeRegistry::class)
class BukkitPropertyTypeRegistry : PropertyTypeRegistry, Services.Fallback {
    val types = mutableObjectSetOf<NpcPropertyType>()

    override fun register(type: NpcPropertyType) {
        val existing = types.firstOrNull { it.id == type.id }

        if (existing != null) {
            types.remove(existing)
        }

        types.add(type)
    }

    override fun unregister(type: NpcPropertyType) {
        types.remove(type)
    }

    override fun get(id: String): NpcPropertyType? {
        return types.firstOrNull { it.id == id }
    }

    override fun getIds(): ObjectSet<String> {
        return types.map { it.id }.toObjectSet()
    }
}

