package dev.slne.surf.npc.paper

import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProfile
import com.github.retrooper.packetevents.protocol.entity.data.EntityData
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.util.Vector3d
import com.github.retrooper.packetevents.wrapper.PacketWrapper
import com.github.retrooper.packetevents.wrapper.play.server.*
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.NpcPose
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.paper.util.buildMetaData
import dev.slne.surf.npc.paper.util.buildNullInfo
import dev.slne.surf.npc.paper.util.emptyComponent
import dev.slne.surf.npc.paper.util.toEntityPose
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.EntityType
import java.util.*
import com.github.retrooper.packetevents.protocol.world.Location as PacketLocation
import org.bukkit.Location as BukkitLocation

sealed class BukkitPackets {
    abstract fun build(): PacketWrapper<*>

    sealed class NpcPackets : BukkitPackets() {
        data class NpcSpawnPacket(
            val entityId: Int,
            val uuid: UUID,
            val location: BukkitLocation,
            val type: EntityType
        ) : NpcPackets() {
            override fun build() = WrapperPlayServerSpawnEntity(
                entityId,
                uuid,
                SpigotConversionUtil.fromBukkitEntityType(type),
                PacketLocation(
                    Vector3d(location.x, location.y, location.z),
                    location.yaw,
                    location.pitch
                ),
                location.yaw,
                0,
                null
            )
        }

        data class NpcMetaDataPacket(
            val npc: Npc
        ) :
            NpcPackets() {
            override fun build() = when (npc.entityType) {
                EntityType.MANNEQUIN -> {
                    val skin = npc.getSkinData() ?: NpcSkin.empty()

                    WrapperPlayServerEntityMetadata(
                        npc.id,
                        listOf(
                            buildMetaData(16, EntityDataTypes.BYTE, skin.skinByte()),
                            buildMetaData(
                                17, EntityDataTypes.RESOLVABLE_PROFILE, ItemProfile(
                                    npc.uniqueName,
                                    npc.npcUuid,
                                    listOf(
                                        ItemProfile.Property(
                                            "textures",
                                            skin.value,
                                            skin.signature
                                        )
                                    )
                                )
                            ),
                            buildMetaData(18, EntityDataTypes.BOOLEAN, true),
                            buildMetaData(
                                19,
                                EntityDataTypes.OPTIONAL_ADV_COMPONENT,
                                Optional.of(npc.getDisplayName())
                            ),
                            buildMetaData(0, EntityDataTypes.BYTE, 0x02.toByte()),
                        )
                    )
                }

                else -> {
                    WrapperPlayServerEntityMetadata(
                        npc.id,
                        listOf(
                            buildMetaData(
                                2,
                                EntityDataTypes.OPTIONAL_ADV_COMPONENT,
                                Optional.of(npc.getDisplayName())
                            ),
                            buildMetaData(3, EntityDataTypes.BOOLEAN, true),
                        )
                    )
                }
            }
        }

        data class NpcPoseChangePacket(val entityId: Int, val pose: NpcPose) : NpcPackets() {
            override fun build() = WrapperPlayServerEntityMetadata(
                entityId,
                listOf(EntityData(6, EntityDataTypes.ENTITY_POSE, pose.toEntityPose()))
            )
        }

        data class NpcTeleportPacket(val entityId: Int, val location: BukkitLocation) :
            NpcPackets() {
            override fun build() = WrapperPlayServerEntityTeleport(
                entityId,
                SpigotConversionUtil.fromBukkitLocation(location),
                false
            )
        }

        data class NpcRotationPacket(val entityId: Int, val yaw: Float, val pitch: Float) :
            NpcPackets() {
            override fun build() = WrapperPlayServerEntityRotation(entityId, yaw, pitch, true)
        }

        data class NpcHeadRotationPacket(val entityId: Int, val yaw: Float) : NpcPackets() {
            override fun build() = WrapperPlayServerEntityHeadLook(entityId, yaw)
        }

        data class NpcInfoRemovePacket(val npcUuid: UUID) : NpcPackets() {
            override fun build() = WrapperPlayServerPlayerInfoRemove(npcUuid)
        }

        class NpcDestroyPacket(vararg val entityIds: Int) : NpcPackets() {
            override fun build() = WrapperPlayServerDestroyEntities(*entityIds)
        }
    }

    sealed class NpcNameTagPackets : BukkitPackets() {
        data class NameTagSpawnPacket(
            val entityId: Int,
            val uuid: UUID,
            val location: BukkitLocation
        ) : NpcNameTagPackets() {
            override fun build() = WrapperPlayServerSpawnEntity(
                entityId,
                uuid,
                EntityTypes.TEXT_DISPLAY,
                PacketLocation(Vector3d(location.x, location.y + 2, location.z), 0f, 0f),
                0f,
                0,
                null
            )
        }

        data class NameTagMetaDataPacket(val entityId: Int, val displayName: Component) :
            NpcNameTagPackets() {
            override fun build() = WrapperPlayServerEntityMetadata(
                entityId,
                listOf(
                    buildMetaData(23, EntityDataTypes.ADV_COMPONENT, displayName),
                    buildMetaData(15, EntityDataTypes.BYTE, 3.toByte()),
                    buildMetaData(27, EntityDataTypes.BYTE, 0x02.toByte())
                )
            )
        }

        data class NameTagCorrectionPacket(
            val entityId: Int,
            val npcLocation: BukkitLocation,
            val npcPose: NpcPose
        ) : NpcNameTagPackets() {
            override fun build() = WrapperPlayServerEntityTeleport(
                entityId,
                SpigotConversionUtil.fromBukkitLocation(
                    calculateNametagLocation(npcPose, npcLocation).add(0.0, 2.0, 0.0)
                ),
                false
            )
        }
    }

    sealed class NpcExtraPackets : BukkitPackets() {
        data class ExtraSpawnPacket(val npc: Npc, val location: BukkitLocation) :
            NpcExtraPackets() {
            override fun build() = WrapperPlayServerSpawnEntity(
                npc.npcSittingId,
                npc.npcSittingUuid,
                EntityTypes.ARMOR_STAND,
                SpigotConversionUtil.fromBukkitLocation(location.clone().subtract(0.0, 2.0, 0.0)),
                0f,
                0,
                null
            )
        }

        data class ExtraMetaDataPacket(val npc: Npc) : NpcExtraPackets() {
            override fun build() = WrapperPlayServerEntityMetadata(
                npc.npcSittingId,
                listOf(buildMetaData(0, EntityDataTypes.BYTE, 0x20.toByte()))
            )
        }

        data class ExtraMountPacket(val npc: Npc) : NpcExtraPackets() {
            override fun build() =
                WrapperPlayServerSetPassengers(npc.npcSittingId, intArrayOf(npc.id))
        }

        data class ExtraDestroyPacket(val npc: Npc) : NpcExtraPackets() {
            override fun build() = WrapperPlayServerDestroyEntities(npc.npcSittingId)
        }
    }

    sealed class NpcTeamPackets : BukkitPackets() {
        data class TeamCreatePacket(val teamName: String, val displayName: Component) :
            NpcTeamPackets() {
            override fun build() = WrapperPlayServerTeams(
                teamName,
                WrapperPlayServerTeams.TeamMode.CREATE,
                WrapperPlayServerTeams.ScoreBoardTeamInfo(
                    displayName,
                    emptyComponent(),
                    emptyComponent(),
                    WrapperPlayServerTeams.NameTagVisibility.NEVER,
                    WrapperPlayServerTeams.CollisionRule.ALWAYS,
                    NamedTextColor.RED,
                    WrapperPlayServerTeams.OptionData.NONE
                )
            )
        }

        data class TeamAddEntityPacket(val teamName: String, val entityUuid: String) :
            NpcTeamPackets() {
            override fun build() = WrapperPlayServerTeams(
                teamName,
                WrapperPlayServerTeams.TeamMode.ADD_ENTITIES,
                buildNullInfo(),
                entityUuid
            )
        }
    }
}

private fun calculateNametagLocation(
    npcPose: NpcPose,
    npcLocation: BukkitLocation
): BukkitLocation =
    when (npcPose) {
        NpcPose.SNEAKING -> npcLocation.clone().subtract(0.0, 0.2, 0.0)
        NpcPose.SITTING -> npcLocation.clone().subtract(0.0, 0.62, 0.0)
        NpcPose.SWIMMING, NpcPose.FALL_FLYING, NpcPose.SLEEPING -> npcLocation.clone()
            .subtract(0.0, 1.62, 0.0)

        else -> npcLocation.clone()
    }

