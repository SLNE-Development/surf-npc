@file:Suppress("UNCHECKED_CAST")

package dev.slne.surf.npc.api.dsl

import dev.slne.surf.api.core.messages.builder.SurfComponentBuilder
import dev.slne.surf.api.core.util.mutableObject2ObjectMapOf
import dev.slne.surf.api.core.util.mutableObjectListOf
import dev.slne.surf.npc.api.event.NpcEvent
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.api.surfNpcApi
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.entity.EntityType
import java.util.*
import kotlin.reflect.KClass

/**
 * Builder class for creating NPCs using a DSL.
 */
class NpcDslBuilder {
    /**
     * The display name of the NPC.
     */
    lateinit var displayName: SurfComponentBuilder.() -> Unit

    /**
     * The unique name of the NPC.
     */
    lateinit var uniqueName: String

    /**
     * The skin of the NPC.
     */
    lateinit var skin: NpcSkin

    lateinit var type: EntityType

    /**
     * The location of the NPC.
     */
    lateinit var location: Location

    /**
     * Whether the NPC is global. Defaults to true.
     */
    var viewers: ObjectSet<UUID>? = null

    /**
     * The rotation type of the NPC. Defaults to PER_PLAYER.
     */
    var rotationType: NpcRotationType = NpcRotationType.PER_PLAYER

    /**
     * Whether the NPC should be persistent. Defaults to false.
     */
    var persistent: Boolean = false

    /**
     * Whether the NPC should glow. Defaults to false.
     */
    var glowing: Boolean = false

    /**
     * The color of the glow effect. Defaults to white.
     */
    var glowingColor: NamedTextColor = NamedTextColor.WHITE

    internal val eventHandlers =
        mutableObject2ObjectMapOf<KClass<out NpcEvent>, ObjectList<(NpcEvent) -> Unit>>()

    /**
     * Registers an event handler for a specific type of `NpcEvent`.
     *
     * @param T The type of the `NpcEvent` this handler will process.
     * @param eventClass The class of the event type to handle.
     * @param handler The function that defines the logic for handling the specified type of `NpcEvent`.
     */
    fun <T : NpcEvent> withEventHandler(eventClass: KClass<T>, handler: (T) -> Unit) {
        eventHandlers.computeIfAbsent(eventClass) { mutableObjectListOf() }
            .add { ev -> handler(ev as T) }
    }

    /**
     * Registers an event handler for a specific type of `NpcEvent`.
     *
     * @param T The type of `NpcEvent` for which the handler is being registered.
     * @param handler A lambda function that defines the behavior when the event of type `T` is triggered.
     */
    inline fun <reified T : NpcEvent> withEventHandler(noinline handler: (T) -> Unit) {
        withEventHandler(T::class, handler)
    }

    fun displayName(block: SurfComponentBuilder.() -> Unit) {
        displayName = block
    }


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
}

/**
 * Creates an NPC location using a DSL block.
 *
 * @param block The DSL block for configuring the location.
 * @return The created NPC location.
 */
fun location(block: LocationBuilder.() -> Unit): Location {
    return LocationBuilder().apply(block).build()
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
suspend fun fetchedSkin(name: String): NpcSkin {
    return surfNpcApi.fetchSkin(name)
}

fun npc(block: NpcDslBuilder.() -> Unit): Npc {
    val builder = NpcDslBuilder().apply(block)
    val npc = surfNpcApi.createNpc(
        displayName = SurfComponentBuilder.builder().apply(builder.displayName).build(),
        uniqueName = builder.uniqueName,
        type = builder.type,
        skin = builder.skin,
        location = builder.location,
        viewers = builder.viewers,
        rotationType = builder.rotationType,
        persistent = builder.persistent
    )

    builder.eventHandlers.forEach { (eventClass, handlersList) ->
        handlersList.forEach { handler ->
            npc.addEventHandler(eventClass as KClass<NpcEvent>) { ev ->
                handler(ev)
            }
        }
    }

    return npc
}
