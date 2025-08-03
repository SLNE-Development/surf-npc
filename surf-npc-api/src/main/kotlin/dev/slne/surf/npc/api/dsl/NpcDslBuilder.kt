package dev.slne.surf.npc.api.dsl

import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.npc.rotation.NpcRotation
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.api.surfNpcApi
import net.kyori.adventure.text.Component

/**
 * Builder class for creating NPCs using a DSL.
 */
class NpcDslBuilder {
    /**
     * The display name of the NPC.
     */
    lateinit var displayName: Component

    /**
     * The unique name of the NPC.
     */
    lateinit var uniqueName: String

    /**
     * The skin of the NPC.
     */
    lateinit var skin: NpcSkin

    /**
     * The location of the NPC.
     */
    lateinit var location: NpcLocation

    /**
     * Whether the NPC is global. Defaults to true.
     */
    var global: Boolean = true

    /**
     * The rotation type of the NPC. Defaults to PER_PLAYER.
     */
    var rotationType: NpcRotationType = NpcRotationType.PER_PLAYER

    /**
     * The fixed rotation of the NPC, if applicable.
     */
    var fixedRotation: NpcRotation? = null

    /**
     * Configures the skin of the NPC using a DSL block.
     *
     * @param block The DSL block for configuring the skin.
     */
    fun skin(block: SkinBuilder.() -> Unit) {
        skin = SkinBuilder().apply(block).build()
    }

    /**
     * Configures the location of the NPC using a DSL block.
     *
     * @param block The DSL block for configuring the location.
     */
    fun location(block: LocationBuilder.() -> Unit) {
        location = LocationBuilder().apply(block).build()
    }

    /**
     * Configures the fixed rotation of the NPC using a DSL block.
     *
     * @param block The DSL block for configuring the fixed rotation.
     */
    fun fixedRotation(block: RotationBuilder.() -> Unit) {
        fixedRotation = RotationBuilder().apply(block).build()
    }
}

/**
 * Creates an NPC location using a DSL block.
 *
 * @param block The DSL block for configuring the location.
 * @return The created NPC location.
 */
fun location(block: LocationBuilder.() -> Unit): NpcLocation {
    return LocationBuilder().apply(block).build()
}

/**
 * Creates an NPC rotation using a DSL block.
 *
 * @param block The DSL block for configuring the rotation.
 * @return The created NPC rotation.
 */
fun rotation(block: RotationBuilder.() -> Unit): NpcRotation {
    return RotationBuilder().apply(block).build()
}

/**
 * Creates an NPC property using a DSL block.
 *
 * @param block The DSL block for configuring the property.
 * @return The created NPC property.
 */
fun npcProperty(block: NpcPropertyBuilder.() -> Unit): NpcProperty {
    return NpcPropertyBuilder().apply(block).build()
}

/**
 * Creates an NPC skin using a DSL block.
 *
 * @param block The DSL block for configuring the skin.
 * @return The created NPC skin.
 */
fun skin(block: SkinBuilder.() -> Unit): NpcSkin {
    return SkinBuilder().apply(block).build()
}

/**
 * Retrieves an NPC skin by name asynchronously.
 *
 * @param name The name of the skin.
 * @return The retrieved NPC skin.
 */
suspend fun skin(name: String): NpcSkin {
    return surfNpcApi.getSkin(name)
}

/**
 * Creates an NPC using a DSL block.
 *
 * @param block The DSL block for configuring the NPC.
 * @return The result of the NPC creation.
 */
fun npc(block: NpcDslBuilder.() -> Unit): NpcCreationResult {
    val builder = NpcDslBuilder().apply(block)
    return surfNpcApi.createNpc(
        displayName = builder.displayName,
        uniqueName = builder.uniqueName,
        skin = builder.skin,
        location = builder.location,
        global = builder.global,
        rotationType = builder.rotationType,
        fixedRotation = builder.fixedRotation
    )
}