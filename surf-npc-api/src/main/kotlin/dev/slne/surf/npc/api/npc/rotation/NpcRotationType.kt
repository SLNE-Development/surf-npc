package dev.slne.surf.npc.api.npc.rotation

/**
 * Enum representing the type of rotation for an NPC.
 */
enum class NpcRotationType {
    /**
     * Fixed rotation type where the NPC's rotation does not change.
     */
    FIXED,

    /**
     * Per-player rotation type where the NPC's rotation is customized for each player.
     */
    PER_PLAYER
}