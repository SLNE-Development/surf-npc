package dev.slne.surf.npc.api.npc.property

import dev.slne.surf.npc.api.surfNpcApi

/**
 * Represents the type of a property associated with an NPC.
 */
interface NpcPropertyType {
    val id: String

    fun encode(value: Any): String
    fun decode(value: String): Any

    object Types {
        val BOOLEAN_TYPE get() = surfNpcApi.getPropertyTypeOrThrow(BOOLEAN_ID)
        val INT_TYPE get() = surfNpcApi.getPropertyTypeOrThrow(INT_ID)
        val LONG_TYPE get() = surfNpcApi.getPropertyTypeOrThrow(LONG_ID)
        val STRING_TYPE get() = surfNpcApi.getPropertyTypeOrThrow(STRING_ID)
        val DOUBLE_TYPE get() = surfNpcApi.getPropertyTypeOrThrow(DOUBLE_ID)
        val FLOAT_TYPE get() = surfNpcApi.getPropertyTypeOrThrow(FLOAT_ID)
        val UUID_TYPE get() = surfNpcApi.getPropertyTypeOrThrow(UUID_ID)
        val NAMED_TEXT_COLOR_TYPE get() = surfNpcApi.getPropertyTypeOrThrow(NAMED_TEXT_COLOR_ID)
        val COMPONENT_TYPE get() = surfNpcApi.getPropertyTypeOrThrow(COMPONENT_ID)
        val SKIN_DATA_TYPE get() = surfNpcApi.getPropertyTypeOrThrow(SKIN_DATA_ID)
        val LOCATION_TYPE get() = surfNpcApi.getPropertyTypeOrThrow(LOCATION_ID)
        val ROTATION_TYPE_TYPE get() = surfNpcApi.getPropertyTypeOrThrow(ROTATION_TYPE_ID)

        const val BOOLEAN_ID = "boolean"

        /**
         * Represents an integer property type.
         */
        const val INT_ID = "int"

        /**
         * Represents a long property type.
         */
        const val LONG_ID = "long"

        /**
         * Represents a string property type.
         */
        const val STRING_ID = "string"

        /**
         * Represents a double property type.
         */
        const val DOUBLE_ID = "double"

        /**
         * Represents a float property type.
         */
        const val FLOAT_ID = "float"

        /**
         * Represents a UUID property type.
         */
        const val UUID_ID = "uuid"

        /**
         * Represents a component property type.
         */
        const val COMPONENT_ID = "component"

        /**
         * Represents a named text color property type.
         */
        const val NAMED_TEXT_COLOR_ID = "named_text_color"

        /**
         * Represents a location property type for an NPC.
         */
        const val LOCATION_ID = "npc_location"

        /**
         * Represents a skin data property type for an NPC.
         */
        const val SKIN_DATA_ID = "skin_data"

        const val ROTATION_TYPE_ID = "rotation_type"
    }
}