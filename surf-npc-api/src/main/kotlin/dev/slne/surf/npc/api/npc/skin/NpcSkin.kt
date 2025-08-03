package dev.slne.surf.npc.api.npc.skin

/**
 * Data class representing the skin data of an NPC.
 *
 * @property value The value of the skin data.
 * @property signature The signature of the skin data.
 */
interface NpcSkin {
    val ownerName: String
    val value: String
    val signature: String
}