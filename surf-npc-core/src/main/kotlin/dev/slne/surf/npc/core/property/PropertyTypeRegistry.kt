package dev.slne.surf.npc.core.property

import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet

/**
 * Registry for managing NPC property types.
 */
interface PropertyTypeRegistry {
    /**
     * Registers a new NPC property type.
     *
     * @param type The property type to register.
     */
    fun register(type: NpcPropertyType)

    /**
     * Unregisters an existing NPC property type.
     *
     * @param type The property type to unregister.
     */
    fun unregister(type: NpcPropertyType)

    /**
     * Retrieves an NPC property type by its unique identifier.
     *
     * @param id The unique identifier of the property type.
     * @return The property type associated with the identifier, or null if not found.
     */
    fun get(id: String): NpcPropertyType?

    /**
     * Retrieves all registered property type identifiers.
     *
     * @return A set of all property type identifiers.
     */
    fun getIds(): ObjectSet<String>

    companion object {
        /**
         * The instance of the PropertyTypeRegistry.
         */
        val INSTANCE = requiredService<PropertyTypeRegistry>()
    }
}

/**
 * Provides access to the singleton instance of the PropertyTypeRegistry.
 */
val propertyTypeRegistry get() = PropertyTypeRegistry.INSTANCE