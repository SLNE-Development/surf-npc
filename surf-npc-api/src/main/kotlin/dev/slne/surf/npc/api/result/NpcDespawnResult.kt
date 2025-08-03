package dev.slne.surf.npc.api.result

/**
 * Enum representing the result of an NPC respawn attempt.
 */
enum class NpcDespawnResult {
    /** Indicates that the NPC was successfully respawned. */
    SUCCESS,

    /** Indicates that the NPC respawn failed because the NPC does not exist. */
    FAILED_NOT_EXIST,

    /** Indicates that the NPC respawn failed because the NPC is already spawned. */
    FAILED_ALREADY_SPAWNED,

    /** Indicates that the NPC respawn failed due to no valid location being available. */
    FAILED_NO_LOCATION,

    /** Indicates that the NPC respawn failed due to an unspecified reason. */
    FAILED_OTHER
}