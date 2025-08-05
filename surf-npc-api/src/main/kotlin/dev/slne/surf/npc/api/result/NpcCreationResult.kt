package dev.slne.surf.npc.api.result

import dev.slne.surf.npc.api.npc.Npc

/**
 * Represents the result of an NPC creation attempt.
 */
sealed class NpcCreationResult {
    /**
     * Indicates that the NPC was created successfully.
     *
     * @property npc The created NPC instance.
     */
    data class Success(val npc: Npc) : NpcCreationResult()

    /**
     * Indicates that the NPC creation failed.
     *
     * @property reason The reason for the failure.
     */
    data class Failure(val reason: NpcCreationFailureReason) : NpcCreationResult()
}

/**
 * Enumerates possible reasons for NPC creation failure.
 */
enum class NpcCreationFailureReason {
    /** An NPC with the same identifier already exists. */
    ALREADY_EXISTS,

    /** The specified location is invalid. */
    INVALID_LOCATION,

    /** The specified name is invalid. */
    INVALID_NAME,

    /** The specified skin is invalid. */
    INVALID_SKIN,

    /** The failure reason is unknown. */
    UNKNOWN
}