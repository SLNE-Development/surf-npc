package dev.slne.surf.npc.api.npc.rotation

/**
 * Represents the rotation of an NPC in the game world.
 *
 * @property yaw The yaw (horizontal rotation) of the NPC.
 * @property pitch The pitch (vertical rotation) of the NPC.
 */
interface NpcRotation {
    val yaw: Float
    val pitch: Float
}