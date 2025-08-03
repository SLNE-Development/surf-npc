package dev.slne.surf.npc.api.dsl

import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotation
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.api.surfNpcApi

/**
 * Builder class for creating NPC skins.
 */
class SkinBuilder {
    /**
     * The name of the skin's owner.
     */
    lateinit var ownerName: String

    /**
     * The value of the skin.
     */
    lateinit var value: String

    /**
     * The signature of the skin.
     */
    lateinit var signature: String

    /**
     * Builds the NPC skin.
     *
     * @return The constructed NPC skin.
     */
    fun build(): NpcSkin = object : NpcSkin {
        override val ownerName = this@SkinBuilder.ownerName
        override val value = this@SkinBuilder.value
        override val signature = this@SkinBuilder.signature
    }
}

/**
 * Builder class for creating NPC locations.
 */
class LocationBuilder {
    /**
     * The x-coordinate of the location.
     */
    var x: Double = 0.0

    /**
     * The y-coordinate of the location.
     */
    var y: Double = 0.0

    /**
     * The z-coordinate of the location.
     */
    var z: Double = 0.0

    /**
     * The name of the world where the location is situated.
     */
    lateinit var world: String

    /**
     * Builds the NPC location.
     *
     * @return The constructed NPC location.
     */
    fun build(): NpcLocation = object : NpcLocation {
        override val x = this@LocationBuilder.x
        override val y = this@LocationBuilder.y
        override val z = this@LocationBuilder.z
        override val world = this@LocationBuilder.world
    }
}

/**
 * Builder class for creating NPC rotations.
 */
class RotationBuilder {
    /**
     * The yaw value of the rotation.
     */
    var yaw: Float = 0f

    /**
     * The pitch value of the rotation.
     */
    var pitch: Float = 0f

    /**
     * Builds the NPC rotation.
     *
     * @return The constructed NPC rotation.
     */
    fun build(): NpcRotation = object : NpcRotation {
        override val yaw = this@RotationBuilder.yaw
        override val pitch = this@RotationBuilder.pitch
    }
}

/**
 * Builder class for creating NPC properties.
 */
class NpcPropertyBuilder {
    /**
     * The key of the property.
     */
    var key: String = ""

    /**
     * The value of the property.
     */
    var value: Any = ""

    /**
     * The type of the property. Defaults to the "string" type.
     */
    var type: NpcPropertyType = surfNpcApi.getPropertyType("string") ?: error("Default property type 'string' not found")

    /**
     * Builds the NPC property.
     *
     * @return The constructed NPC property.
     */
    fun build(): NpcProperty = object : NpcProperty {
        override val key = this@NpcPropertyBuilder.key
        override val value = this@NpcPropertyBuilder.value
        override val type = this@NpcPropertyBuilder.type
    }
}