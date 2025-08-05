package dev.slne.surf.npc.api.result

import dev.slne.surf.npc.api.npc.Npc

/**
 * Represents the result of an NPC respawn attempt.
 */
sealed class NpcRespawnResult {
    /**
     * Indicates that the NPC was successfully respawned.
     *
     * @property npc The NPC instance that was successfully respawned.
     */
    data class Success(val npc: Npc) : NpcRespawnResult()

    /**
     * Indicates that the NPC respawn attempt failed.
     *
     * @property reason The reason for the respawn failure.
     */
    data class Failure(val reason: NpcRespawnFailureReason) : NpcRespawnResult()

    /**
     * Checks if the result represents a successful NPC respawn.
     *
     * @return `true` if the result is a success, otherwise `false`.
     */
    fun isSuccess(): Boolean {
        return this is Success
    }

    /**
     * Checks if the result represents a failed NPC respawn.
     *
     * @return `true` if the result is a failure, otherwise `false`.
     */
    fun isFailure(): Boolean {
        return this is Failure
    }
}

enum class NpcRespawnFailureReason {
    /** The NPC does not exist. */
    NOT_EXIST,

    /** The NPC is already spawned. */
    ALREADY_SPAWNED,

    /** No valid location is available for respawning the NPC. */
    NO_LOCATION,

    /** The failure reason is unknown. */
    UNKNOWN
}