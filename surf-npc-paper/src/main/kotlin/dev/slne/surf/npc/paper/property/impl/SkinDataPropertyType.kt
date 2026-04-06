package dev.slne.surf.npc.paper.property.impl

import dev.slne.surf.api.core.util.toObjectSet
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.api.npc.skin.NpcSkinPart

class SkinDataPropertyType(override val id: String) : NpcPropertyType {
    override fun encode(value: Any): String {
        require(value is NpcSkin) { "Expected NpcSkin, got ${value::class}" }
        return "${value.ownerName}:${value.value}:${value.signature}:${value.parts.joinToString(",") { it.name }}"
    }

    override fun decode(value: String): NpcSkin {
        return value.split(":").let {
            require(it.size >= 3) { "Invalid skin data format: $value" }
            val ownerName = it[0]
            val skinValue = it[1]
            val signature = it[2]
            val parts = it.getOrNull(3)?.split(",")?.map { part -> NpcSkinPart.valueOf(part) }
                ?: emptyList()

            NpcSkin(ownerName, skinValue, signature, parts.toObjectSet())
        }
    }
}