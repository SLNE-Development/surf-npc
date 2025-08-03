package dev.slne.surf.npc.api

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.result.NpcDeletionResult
import dev.slne.surf.npc.api.npc.rotation.NpcRotation
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import java.util.UUID

/**
 * API for managing NPCs (Non-Player Characters) in the Surf framework.
 */
interface SurfNpcApi {

    /**
     * Creates a new NPC.
     *
     * @param displayName The display name of the NPC.
     * @param uniqueName The unique name of the NPC.
     * @param skin The skin data of the NPC.
     * @param location The location of the NPC.
     * @param global Whether the NPC is global (default: true).
     * @param rotationType The rotation type of the NPC (default: FIXED).
     * @param fixedRotation The fixed rotation of the NPC, if applicable (default: null).
     * @return The result of the NPC creation.
     */
    fun createNpc(
        displayName: Component,
        uniqueName: String,
        skin: NpcSkin,
        location: NpcLocation,
        global: Boolean = true,
        rotationType: NpcRotationType = NpcRotationType.PER_PLAYER,
        fixedRotation: NpcRotation? = null
    ): NpcCreationResult

    /**
     * Deletes an existing NPC.
     *
     * @param npc The NPC to delete.
     * @return The result of the NPC deletion.
     */
    fun deleteNpc(npc: Npc): NpcDeletionResult

    /**
     * Shows an NPC to a specific player.
     *
     * @param npc The NPC to show.
     * @param uuid The UUID of the player.
     */
    fun showNpc(npc: Npc, uuid: UUID)

    /**
     * Hides an NPC from a specific player.
     *
     * @param npc The NPC to hide.
     * @param uuid The UUID of the player.
     */
    fun hideNpc(npc: Npc, uuid: UUID)

    /**
     * Sets the skin of an NPC.
     *
     * @param npc The NPC to update.
     * @param skin The new skin data.
     */
    fun setSkin(npc: Npc, skin: NpcSkin)

    /**
     * Sets the rotation type of an NPC.
     *
     * @param npc The NPC to update.
     * @param rotationType The new rotation type.
     */
    fun setRotationType(npc: Npc, rotationType: NpcRotationType)

    /**
     * Sets the rotation of an NPC.
     *
     * @param npc The NPC to update.
     * @param rotation The new rotation.
     */
    fun setRotation(npc: Npc, rotation: NpcRotation)

    /**
     * Retrieves the properties of an NPC.
     *
     * @param npc The NPC whose properties are to be retrieved.
     * @return A set of properties associated with the NPC.
     */
    fun getProperties(npc: Npc): ObjectSet<NpcProperty>

    /**
     * Adds a property to an NPC.
     *
     * @param npc The NPC to update.
     * @param property The property to add.
     * @return True if the property was added, false otherwise.
     */
    fun addProperty(npc: Npc, property: NpcProperty): Boolean

    /**
     * Removes a property from an NPC.
     *
     * @param npc The NPC to update.
     * @param key The property key
     * @return True if the property was removed, false otherwise.
     */
    fun removeProperty(npc: Npc, key: String): Boolean

    /**
     * Creates a new property for an NPC.
     *
     * @param key The key of the property.
     * @param value The value of the property.
     * @param type The type of the property.
     * @return The created property.
     */
    fun createProperty(
        key: String,
        value: Any,
        type: NpcPropertyType
    ): NpcProperty

    suspend fun getSkin(name: String): NpcSkin

    /**
     * Retrieves an NPC by its ID.
     *
     * @param id The ID of the NPC.
     * @return The NPC with the specified ID, or null if not found.
     */
    fun getNpc(id: Int): Npc?

    /**
     * Retrieves an NPC by its unique name.
     *
     * @param uniqueName The unique name of the NPC.
     * @return The NPC with the specified unique name, or null if not found.
     */
    fun getNpc(uniqueName: String): Npc?

    /**
     * Retrieves all NPCs.
     *
     * @return A list of all NPCs.
     */
    fun getNpcs(): ObjectList<Npc>

    /**
     * Despawns all NPCs.
     */
    fun despawnAllNpcs()

    /**
     * Creates a new rotation.
     *
     * @param yaw The yaw value of the rotation.
     * @param pitch The pitch value of the rotation.
     * @return The created rotation.
     */
    fun createRotation(
        yaw: Float,
        pitch: Float
    ): NpcRotation

    /**
     * Creates new skin data.
     *
     * @param owner The owner of the skin.
     * @param value The value of the skin.
     * @param signature The signature of the skin.
     * @return The created skin data.
     */
    fun createSkinData(
        owner: String,
        value: String,
        signature: String
    ): NpcSkin

    /**
     * Creates a new location.
     *
     * @param x The x-coordinate of the location.
     * @param y The y-coordinate of the location.
     * @param z The z-coordinate of the location.
     * @param worldName The name of the world.
     * @return The created location.
     */
    fun createLocation(
        x: Double,
        y: Double,
        z: Double,
        worldName: String
    ): NpcLocation

    fun registerPropertyType(type: NpcPropertyType)
    fun unregisterPropertyType(type: NpcPropertyType)

    fun getPropertyType(id: String): NpcPropertyType?

    companion object {
        /**
         * The instance of the SurfNpcApi.
         */
        val INSTANCE = requiredService<SurfNpcApi>()
    }
}

/**
 * A shortcut to access the SurfNpcApi instance.
 */
val surfNpcApi get() = SurfNpcApi.INSTANCE