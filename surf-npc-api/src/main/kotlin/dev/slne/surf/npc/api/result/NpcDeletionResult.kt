package dev.slne.surf.npc.api.result

/**
 * Enum representing the result of an NPC deletion attempt.
 */
enum class NpcDeletionResult {
    /** Indicates that the NPC was successfully deleted. */
    SUCCESS,

    /** Indicates that the NPC deletion failed because the NPC was not found. */
    FAILED_NOT_FOUND,

    /** Indicates that the NPC deletion failed because the NPC was not spawned. */
    FAILED_NOT_SPAWNED,

    /** Indicates that the NPC deletion failed due to an unspecified reason. */
    FAILED_OTHER
}