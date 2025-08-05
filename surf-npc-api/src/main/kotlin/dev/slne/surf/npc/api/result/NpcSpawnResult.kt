package dev.slne.surf.npc.api.result

import dev.slne.surf.npc.api.npc.Npc

/**
 * Enum representing the result of an NPC spawn attempt.
 */
sealed class NpcSpawnResult {
    data class Success(val npc: Npc) : NpcSpawnResult()
    data class Failure(val reason: NpcSpawnFailureReason) : NpcSpawnResult()
}

enum class NpcSpawnFailureReason {
    /** The NPC does not exist. */
    NOT_EXIST,

    /** The NPC is already spawned. */
    ALREADY_SPAWNED,

    /** No valid location is available for spawning the NPC. */
    NO_LOCATION,

    /** The failure reason is unknown. */
    UNKNOWN
}