package dev.slne.surf.npc.api.npc.property

/**
 * Represents a property of an NPC.
 *
 * @property key The unique key identifying the property.
 * @property value The value associated with the property.
 * @property type The type of the property.
 */
interface NpcProperty {
    val key: String
    val value: Any
    val type: NpcPropertyType

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
         * The global visibility of the NPC.
         */
        const val VISIBILITY_GLOBAL = "visibility_global"

        /**
         * The type of rotation for the NPC.
         */
        const val ROTATION_TYPE = "rotation_type"

        /**
         * The fixed rotation value for the NPC.
         */
        const val ROTATION_FIXED = "rotation"

        /**
         * The persistence of the NPC.
         */
        const val PERSISTENCE = "persistence"

        /**
         * The glowing effect status of the NPC.
         */
        const val GLOWING_ENABLED = "glowing_enabled"

        /**
         * The color of the glowing effect for the NPC.
         */
        const val GLOWING_COLOR = "glowing_color"

        /**
         * The type of the creator of the NPC.
         */
        const val CREATOR_TYPE = "creator_type"
    }
}