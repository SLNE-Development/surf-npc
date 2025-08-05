package dev.slne.surf.npc.api.result

import dev.slne.surf.npc.api.npc.Npc

/**
 * Represents the result of an NPC despawn attempt.
 */
sealed class NpcDespawnResult {
    data class Success(val npc: Npc) : NpcDespawnResult()
    data class Failure(val reason: NpcDespawnFailureReason) : NpcDespawnResult()
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