package dev.slne.surf.npc.api.npc

enum class NpcPose(val id: Int) {
    STANDING(0),
    FALL_FLYING(1),
    SLEEPING(2),
    SWIMMING(3),
    SPIN_ATTACK(4),
    SNEAKING(5),
    LONG_JUMPING(6),
    DYING(7),
    CROAKING(8),
    USING_TONGUE(9),
    SITTING(10),
    ROARING(11),
    SNIFFING(12),
    EMERGING(13),
    DIGGING(14),
    SLIDING(15),
    SHOOTING(16),
    INHALING(17);

    companion object {
        private val BY_ID = entries.associateBy(NpcPose::id)

        fun fromId(id: Int): NpcPose? = BY_ID[id]
        operator fun get(id: Int): NpcPose? = fromId(id)
    }
}
