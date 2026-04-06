package dev.slne.surf.npc.api.npc

/**
 * Represents the type of an NPC creator.
 */
sealed class NpcCreatorType {

    /**
     * Represents a player as the creator of the NPC.
     *
     * @property name The name of the player who created the NPC.
     */
    data class Client(val name: String) : NpcCreatorType()

    /**
     * Represents a plugin as the creator of the NPC.
     *
     * @property name The name of the plugin that created the NPC.
     */
    data class Plugin(val name: String) : NpcCreatorType()

    /**
     * Checks if the creator type is a player.
     *
     * @return `true` if the creator is a player, otherwise `false`.
     */
    fun isClient(): Boolean {
        return this is Client
    }

    /**
     * Checks if the creator type is a plugin.
     *
     * @return `true` if the creator is a plugin, otherwise `false`.
     */
    fun isPlugin(): Boolean {
        return this is Plugin
    }

    /**
     * Returns the name of the creator.
     *
     * @return The name of the creator, whether it's a player or a plugin.
     */

    fun name(): String {
        return when (this) {
            is Client -> name
            is Plugin -> name
        }
    }
}