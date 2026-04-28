package dev.slne.surf.npc.api.npc.property

/**
 * Represents a property of an NPC.
 *
 * @property key The unique key identifying the property.
 * @property value The value associated with the property.
 * @property type The type of the property.
 */
data class NpcProperty(
    val key: String,
    val value: Any,
    val type: NpcPropertyType
) {
    object Internal {
        const val DISPLAYNAME = "displayname"
        const val SKIN_DATA = "skin_data"
        const val LOCATION = "location"
        const val ROTATION_TYPE = "rotation_type"
        const val PERSISTENCE = "persistence"
        const val SCALE = "scale"
        const val POSE = "pose"
    }
}