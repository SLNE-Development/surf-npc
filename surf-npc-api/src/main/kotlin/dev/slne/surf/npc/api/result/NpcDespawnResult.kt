package dev.slne.surf.npc.api.result

import dev.slne.surf.npc.api.npc.Npc

/**
 * Represents the result of an NPC despawn attempt.
 */
sealed class NpcDespawnResult {
    /**
     * Indicates that the NPC was successfully despawned.
     *
     * @property npc The NPC instance that was successfully despawned.
     */
    data class Success(val npc: Npc) : NpcDespawnResult()

    /**
     * Indicates that the NPC despawn attempt failed.
     *
     * @property reason The reason for the despawn failure.
     */
    data class Failure(val reason: NpcDespawnFailureReason) : NpcDespawnResult()

    /**
     * Checks if the result represents a successful NPC despawn.
     *
     * @return `true` if the result is a success, otherwise `false`.
     */
    fun isSuccess(): Boolean {
        return this is Success
    }

    /**
     * Checks if the result represents a failed NPC despawn.
     *
     * @return `true` if the result is a failure, otherwise `false`.
     */
    fun isFailure(): Boolean {
        return this is Failure
    }
}

enum class NpcDespawnFailureReason {
    /** The NPC does not exist. */
    NOT_EXIST,

    /** The NPC is not spawned. */
    NOT_SPAWNED,

    /** The NPC is already despawned. */
    ALREADY_DESPAWNED,

    /** The failure reason is unknown. */
    UNKNOWN
}