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
        /**
         * The display name of the NPC.
         */
        const val DISPLAYNAME = "displayname"

        /**
         * The skin data of the NPC.
         */
        const val SKIN_DATA = "skin_data"

        /**
         * The location of the NPC.
         */
        const val LOCATION = "location"

        /**
         * The type of rotation for the NPC.
         */
        const val ROTATION_TYPE = "rotation_type"

        /**
         * The persistence of the NPC.
         */
        const val PERSISTENCE = "persistence"
    }
}