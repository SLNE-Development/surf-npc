package dev.slne.surf.npc.paper.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class NpcPropertyConfig(
    var type: String = "",
    var value: String = ""
)
