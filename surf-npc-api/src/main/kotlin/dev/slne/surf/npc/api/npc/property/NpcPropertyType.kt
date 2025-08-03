package dev.slne.surf.npc.api.npc.property

/**
 * Represents the type of a property associated with an NPC.
 */
interface NpcPropertyType {
    /**
     * The unique identifier for the property type.
     */
    val id: String

    /**
     * Encodes a value into a string representation.
     *
     * @param value The value to encode.
     * @return The encoded string representation of the value.
     */
    fun encode(value: Any): String

    /**
     * Decodes a string representation into its original value.
     *
     * @param value The string representation to decode.
     * @return The decoded value.
     */
    fun decode(value: String): Any

    /**
     * Contains predefined property types for NPCs.
     */
    object Types {
        /**
         * Represents a boolean property type.
         */
        const val BOOLEAN = "boolean"

        /**
         * Represents an integer property type.
         */
        const val INT = "int"

        /**
         * Represents a long property type.
         */
        const val LONG = "long"

        /**
         * Represents a string property type.
         */
        const val STRING = "string"

        /**
         * Represents a double property type.
         */
        const val DOUBLE = "double"

        /**
         * Represents a float property type.
         */
        const val FLOAT = "float"

        /**
         * Represents a UUID property type.
         */
        const val UUID = "uuid"

        /**
         * Represents a component property type.
         */
        const val COMPONENT = "component"

        /**
         * Represents a named text color property type.
         */
        const val NAMED_TEXT_COLOR = "named_text_color"

        /**
         * Represents a location property type for an NPC.
         */
        const val NPC_LOCATION = "npc_location"

        /**
         * Represents a rotation property type for an NPC.
         */
        const val NPC_ROTATION = "npc_rotation"
    }
}