package dev.slne.surf.npc.core.controller

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.result.NpcDeletionResult
import dev.slne.surf.npc.api.result.NpcRespawnResult
import dev.slne.surf.npc.api.result.NpcSpawnResult
import dev.slne.surf.npc.api.npc.rotation.NpcRotation
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import java.util.UUID

/**
 * Interface for managing NPCs in the game.
 */
interface NpcController {
    /**
     * Creates a new NPC.
     *
     * @param uniqueName The unique name of the NPC.
     * @param displayName The display name of the NPC.
     * @param skinData The skin data for the NPC.
     * @param location The location where the NPC will be spawned.
     * @param rotationType The type of rotation for the NPC.
     * @param rotation The rotation values for the NPC.
     * @param global Whether the NPC is globally visible.
     * @return The result of the NPC creation.
     */
    fun createNpc(uniqueName: String, displayName: Component, skinData: NpcSkin, location: NpcLocation, rotationType: NpcRotationType, rotation: NpcRotation, global: Boolean): NpcCreationResult

    /**
     * Deletes an NPC.
     *
     * @param npc The NPC to delete.
     * @return The result of the NPC deletion.
     */
    fun deleteNpc(npc: Npc): NpcDeletionResult

    /**
     * Registers an NPC.
     *
     * @param npc The NPC to register.
     */
    fun registerNpc(npc: Npc)

    /**
     * Unregisters an NPC.
     *
     * @param npc The NPC to unregister.
     * @return True if the NPC was successfully unregistered, false otherwise.
     */
    fun unregisterNpc(npc: Npc): Boolean

    /**
     * Makes an NPC visible to a specific player.
     *
     * @param npc The NPC to show.
     * @param uuid The UUID of the player.
     * @return The result of the NPC spawn.
     */
    fun showNpc(npc: Npc, uuid: UUID): NpcSpawnResult

    /**
     * Hides an NPC from a specific player.
     *
     * @param npc The NPC to hide.
     * @param uuid The UUID of the player.
     * @return The result of the NPC deletion.
     */
    fun hideNpc(npc: Npc, uuid: UUID): NpcDeletionResult

    /**
     * Respawns an NPC for a specific player.
     *
     * @param npc The NPC to respawn.
     * @param uuid The UUID of the player.
     * @return The result of the NPC respawn.
     */
    fun reShowNpc(npc: Npc, uuid: UUID): NpcRespawnResult

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
     * Sets the rotation values of an NPC.
     *
     * @param npc The NPC to update.
     * @param rotation The new rotation values.
     */
    fun setRotation(npc: Npc, rotation: NpcRotation)

    /**
     * Retrieves an NPC by its ID.
     *
     * @param id The ID of the NPC.
     * @return The NPC, or null if not found.
     */
    fun getNpc(id: Int): Npc?

    /**
     * Retrieves an NPC by its unique name.
     *
     * @param uniqueName The unique name of the NPC.
     * @return The NPC, or null if not found.
     */
    fun getNpc(uniqueName: String): Npc?

    /**
     * Retrieves all registered NPCs.
     *
     * @return A list of all NPCs.
     */
    fun getNpcs(): ObjectList<Npc>

    /**
     * Despawns all NPCs.
     *
     * @return The number of NPCs despawned.
     */
    fun despawnAllNpcs(): Int

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
     * @return True if the property was added successfully, false otherwise.
     */
    fun addProperty(npc: Npc, property: NpcProperty): Boolean

    /**
     * Removes a property from an NPC.
     *
     * @param npc The NPC to update.
     * @param key The key of the property to remove.
     * @return True if the property was removed successfully, false otherwise.
     */
    fun removeProperty(npc: Npc, key: String): Boolean

    companion object {
        /**
         * The instance of the NpcController.
         */
        val INSTANCE = requiredService<NpcController>()
    }
}

/**
 * The instance of the NpcController.
 */
val npcController get() = NpcController.INSTANCE