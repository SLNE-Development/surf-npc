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
         * The owner of the NPC's skin.
         */
        const val SKIN_OWNER = "skin_owner"

        /**
         * The texture of the NPC's skin.
         */
        const val SKIN_TEXTURE = "skin_texture"

        /**
         * The signature of the NPC's skin.
         */
        const val SKIN_SIGNATURE = "skin_signature"

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
    }
}