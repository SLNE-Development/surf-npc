package dev.slne.surf.npc.api.result

/**
 * Enum representing the result of an NPC creation attempt.
 */
enum class NpcCreationResult {
    /** Indicates that the NPC was successfully created. */
    SUCCESS,

    /** Indicates that the NPC creation failed because an NPC with the same identifier already exists. */
    FAILED_ALREADY_EXISTS,

    /** Indicates that the NPC creation failed due to an invalid location. */
    FAILED_INVALID_LOCATION,

    /** Indicates that the NPC creation failed due to an invalid name. */
    FAILED_INVALID_NAME,

    /** Indicates that the NPC creation failed due to an invalid skin. */
    FAILED_INVALID_SKIN,

    /** Indicates that the NPC creation failed due to an unspecified reason. */
    FAILED_OTHER
}