package dev.slne.surf.npc.api.npc

enum class NpcPose(val id: Int, val usable: Boolean = false) {
    STANDING(0, true),
    FALL_FLYING(1, true),
    SLEEPING(2, true),
    SWIMMING(3, true),

    @Deprecated("No effect")
    SPIN_ATTACK(4),
    SNEAKING(5, true),

    @Deprecated("No effect")
    LONG_JUMPING(6),

    @Deprecated("No effect")
    DYING(7),

    @Deprecated("No effect")
    CROAKING(8),

    @Deprecated("No effect")
    USING_TONGUE(9),

    SITTING(10, true),

    @Deprecated("No effect")
    ROARING(11),

    @Deprecated("No effect")
    SNIFFING(12),

    @Deprecated("No effect")
    EMERGING(13),

    @Deprecated("No effect")
    DIGGING(14),

    @Deprecated("No effect")
    SLIDING(15),

    @Deprecated("No effect")
    SHOOTING(16),

    @Deprecated("No effect")
    INHALING(17);

    companion object {
        private val BY_ID = entries.associateBy(NpcPose::id)

        fun fromId(id: Int): NpcPose? = BY_ID[id]
        operator fun get(id: Int): NpcPose? = fromId(id)
    }
}
