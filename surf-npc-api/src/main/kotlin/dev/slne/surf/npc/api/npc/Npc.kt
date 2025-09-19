package dev.slne.surf.npc.api.npc

import dev.slne.surf.npc.api.event.NpcEvent
import dev.slne.surf.npc.api.npc.animation.NpcAnimationType
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass


/**
 * A type alias representing an event handler for NPC-related events.
 *
 * @param T The type of the event that will be handled.
 */
typealias NpcEventHandler<T> = (T) -> Unit

/**
 * Represents a non-player character (NPC) in the game.
 */
interface Npc {
    /**
     * The unique identifier of the NPC.
     */
    val id: Int

    /**
     * The unique name of the NPC.
     */
    val uniqueName: String

    /**
     * The UUID of the NPC.
     */
    val npcUuid: UUID

    /**
     * The unique identifier for the NPC's name tag.
     */
    val nameTagId: Int

    /**
     * The UUID associated with the NPC's name tag.
     */
    val nameTagUuid: UUID

    /**
     * A map of properties associated with the NPC.
     */
    val properties: Object2ObjectMap<String, NpcProperty>

    /**
     * A set of UUIDs representing players who can view the NPC,
     * if the list is null, everyone can see it.
     */
    val viewers: ObjectSet<UUID>?

    /**
     * Spawns the NPC for a specific player.
     *
     * @param uuid The UUID of the player.
     */
    fun spawn(uuid: UUID)

    /**
     * Despawns the NPC for a specific player.
     *
     * @param uuid The UUID of the player.
     */
    fun despawn(uuid: UUID)

    /**
     * Refreshes the NPC's state.
     */
    fun refresh()

    /**
     * Refreshes the rotation of the NPC for a specific player.
     *
     * @param uuid The UUID of the player.
     */
    fun refreshRotation(uuid: UUID)

    /**
     * Deletes the NPC from the game.
     */
    fun delete()

    /**
     * Teleports the NPC to a player's location.
     *
     * @param player The player to teleport the NPC to.
     */
    fun teleport(player: Player)


    /**
     * Retrieves the set of UUIDs representing players who can view the NPC.
     * Returns the viewer list, or if null all online players
     *
     *
     * @return A set of UUIDs of players who can view the NPC, or null if the NPC is visible to all players.
     */
    fun retrieveViewers(): ObjectSet<UUID>

    fun forEachViewer(action: (UUID) -> Unit)

    /**
     * Makes the NPC visible to all players.
     */
    fun show()

    /**
     * Hides the NPC from all players.
     */
    fun hide()

    /**
     * Adds a property to the NPC.
     *
     * @param property The property to add.
     */
    fun addProperty(property: NpcProperty)

    /**
     * Adds multiple properties to the NPC.
     *
     * @param properties A variable number of properties to add, each represented as a Triple containing the key, value, and type.
     */
    fun addProperties(vararg properties: Triple<String, Any, NpcPropertyType>)

    /**
     * Checks if the NPC is static, meaning it has a persistence property set to true.
     * If the NPC is static, it will be persistent across server restarts.
     */
    fun isStatic() =
        properties.any { it.key == NpcProperty.Internal.PERSISTENCE && it.value.value as? Boolean ?: false }

    /**
     * Checks if the NPC is created by a plugin.
     *
     * @return True if the NPC is created by a plugin, false otherwise.
     */
    fun isFromPlugin() =
        properties.any { it.key == NpcProperty.Internal.CREATOR_TYPE && it.value.value is NpcCreatorType.Plugin }


    /**
     * Adds properties to the NPC.
     *
     * @param properties The properties to add.
     */
    fun addProperties(vararg properties: NpcProperty)

    /**
     * Retrieves a property of the NPC by its key.
     *
     * @param key The key of the property.
     * @return The property associated with the key, or null if not found.
     */
    fun getProperty(key: String): NpcProperty?

    /**
     * Retrieves the value of a property by its key and type.
     *
     * @param key The key of the property.
     * @param clazz The class type of the property value.
     * @return The value of the property, or null if not found.
     */
    fun <T : Any> getPropertyValue(key: String, clazz: KClass<T>): T?

    /**
     * Removes a property from the NPC by its key.
     *
     * @param key The key of the property to remove.
     */
    fun removeProperty(key: String)

    /**
     * Checks if the NPC has a specific property.
     *
     * @param key The key of the property.
     * @return True if the property exists, false otherwise.
     */
    fun hasProperty(key: String): Boolean

    /**
     * Clears all properties from the NPC.
     */
    fun clearProperties()

    /**
     * Checks if the NPC has any properties.
     *
     * @return True if the NPC has properties, false otherwise.
     */
    fun hasProperties(): Boolean


    /**
     * Registers an event handler for a specific type of NPC event.
     *
     * @param T The type of the event that the handler will process.
     * @param eventClass The class of the event to handle.
     * @param handler The handler to be invoked when the event occurs.
     */
    fun <T : NpcEvent> addEventHandler(eventClass: KClass<T>, handler: NpcEventHandler<T>)

    /**
     * Removes a previously registered event handler for a specific NPC event type.
     *
     * @param eventClass The class of the event type for which the handler is to be removed.
     * @param handler The handler instance to be removed from the event type.
     */
    fun <T : NpcEvent> removeEventHandler(eventClass: KClass<T>, handler: NpcEventHandler<T>)

    /**
     * Invokes all registered event handlers for the given event.
     *
     * @param event The event to be handled. The event must be a subclass of [NpcEvent].
     */
    fun <T : NpcEvent> callHandlers(event: T)


    /**
     * Plays an animation on the NPC.
     *
     * @param animationType The type of animation to play.
     */
    fun playAnimation(animationType: NpcAnimationType)
}