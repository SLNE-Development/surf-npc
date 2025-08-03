package dev.slne.surf.npc.api.npc.location

/**
 * Represents the location of an NPC in the game world.
 *
 * @property x The X-coordinate of the NPC's location.
 * @property y The Y-coordinate of the NPC's location.
 * @property z The Z-coordinate of the NPC's location.
 * @property world The name of the world where the NPC is located.
 */
interface NpcLocation {
    val x: Double
    val y: Double
    val z: Double
    val world: String
}