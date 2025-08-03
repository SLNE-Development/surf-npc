package dev.slne.surf.npc.api.npc

import dev.slne.surf.npc.api.npc.property.NpcProperty
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

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
     * A set of UUIDs representing players who can view the NPC.
     */
    val viewers: ObjectSet<UUID>

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
}