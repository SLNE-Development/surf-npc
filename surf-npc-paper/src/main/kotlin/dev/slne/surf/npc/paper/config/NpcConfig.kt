package dev.slne.surf.npc.paper.config

import org.bukkit.entity.EntityType
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.util.*

@ConfigSerializable
data class NpcConfig(
    var id: Int = 0,
    var npcUuid: UUID = UUID.randomUUID(),
    var entityType: EntityType = EntityType.ZOMBIE,
    var nameTagId: Int = 0,
    var nameTagUuid: UUID = UUID.randomUUID(),
    var transparentNameTag: Boolean = false,
    var uniqueName: String = "",
    var sittingId: Int = 0,
    var sittingUuid: UUID = UUID.randomUUID(),
    var viewerAmount: Int = -1,
    var viewers: List<UUID> = emptyList(),
    var properties: Map<String, NpcPropertyConfig> = emptyMap()
)
