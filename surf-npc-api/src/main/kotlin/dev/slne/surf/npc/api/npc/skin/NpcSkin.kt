package dev.slne.surf.npc.api.npc.skin

import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet

data class NpcSkin(
    val ownerName: String,
    val value: String,
    val signature: String,
    val parts: ObjectSet<NpcSkinPart>
) {
    fun skinByte(): Byte {
        var skinByte = 0
        if (NpcSkinPart.CAPE in parts) skinByte = skinByte or 0x01
        if (NpcSkinPart.JACKET in parts) skinByte = skinByte or 0x02
        if (NpcSkinPart.LEFT_SLEEVES in parts) skinByte = skinByte or 0x04
        if (NpcSkinPart.RIGHT_SLEEVES in parts) skinByte = skinByte or 0x08
        if (NpcSkinPart.LEFT_PANTS_LEG in parts) skinByte = skinByte or 0x10
        if (NpcSkinPart.RIGHT_PANTS_LEG in parts) skinByte = skinByte or 0x20
        if (NpcSkinPart.HAT in parts) skinByte = skinByte or 0x40
        return skinByte.toByte()
    }

    companion object {
        fun empty() = NpcSkin(
            ownerName = "",
            value = "",
            signature = "",
            parts = mutableObjectSetOf()
        )
    }
}