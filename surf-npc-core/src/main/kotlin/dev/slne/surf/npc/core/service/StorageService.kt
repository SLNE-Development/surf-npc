package dev.slne.surf.npc.core.service

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.surfapi.core.api.util.requiredService
import org.jetbrains.annotations.Blocking
import java.nio.file.Path

/**
 * Service for managing the storage and persistence of NPCs.
 */
interface StorageService {
    /**
     * Initializes the storage service.
     */
    fun initialize()

    /**
     * Loads NPCs from storage.
     *
     * @return The number of NPCs loaded.
     */
    fun loadNpcs(): Int

    /**
     * Saves NPCs to storage.
     *
     * @return The number of NPCs saved.
     */
    fun saveNpcs(): Int

    /**
     * Imports NPC data from a file.
     *
     * @param fileName The name of the file to import.
     * @return True if the import was successful, false otherwise.
     */
    fun import(fileName: String): Boolean

    /**
     * Exports the data of a specific NPC to storage.
     *
     * @param npc The NPC to export.
     */
    fun export(npc: Npc)

    /**
     * Imports all NPC data from storage.
     *
     * @return The number of NPCs imported.
     */
    fun importAll(): Int

    /**
     * Exports all NPC data to storage.
     *
     * @return The number of NPCs exported.
     */
    fun exportAll(): Int

    /**
     * Reloads NPC data from disk.
     *
     * @return The number of NPCs reloaded.
     */
    fun reloadFromDisk(): Int

    /**
     * Saves NPC data to disk.
     *
     * @return The number of NPCs saved.
     */
    fun saveToDisk(): Int

    companion object {
        /**
         * The instance of the StorageService.
         */
        val INSTANCE = requiredService<StorageService>()
    }
}

/**
 * Provides access to the singleton instance of the StorageService.
 */
val storageService get() = StorageService.INSTANCE