package dev.slne.surf.npc.api.dsl

import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.api.npc.skin.NpcSkinPart
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.bukkit.Bukkit
import org.bukkit.Location

/**
 * Builder class for creating NPC skins.
 */
class SkinBuilder {
    lateinit var ownerName: String
    lateinit var value: String
    lateinit var signature: String

    var parts: ObjectSet<NpcSkinPart> = NpcSkinPart.entries.toObjectSet()
    fun build(): NpcSkin = NpcSkin(
        this@SkinBuilder.ownerName,
        this@SkinBuilder.value,
        this@SkinBuilder.signature,
        this@SkinBuilder.parts
    )
}

class LocationBuilder {
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0
    lateinit var world: String

    fun build() = Location(
        Bukkit.getWorld(this@LocationBuilder.world) ?: error("World not found!"),
        this@LocationBuilder.x,
        this@LocationBuilder.y,
        this@LocationBuilder.z
    )
}

class NpcPropertyBuilder {
    var key: String = ""
    var value: Any = ""
    var type: NpcPropertyType = NpcPropertyType.Types.STRING_TYPE


    fun build(): NpcProperty = NpcProperty(
        this@NpcPropertyBuilder.key,
        this@NpcPropertyBuilder.value,
        this@NpcPropertyBuilder.type
    )
}