package dev.slne.surf.npc.api.result

import dev.slne.surf.npc.api.npc.Npc

/**
 * Represents the result of an NPC respawn attempt.
 */
sealed class NpcRespawnResult {
    data class Success(val npc: Npc) : NpcRespawnResult()
    data class Failure(val reason: NpcRespawnFailureReason) : NpcRespawnResult()
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