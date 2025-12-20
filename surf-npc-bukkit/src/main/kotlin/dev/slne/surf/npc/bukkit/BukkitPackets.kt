package dev.slne.surf.npc.bukkit

import com.github.retrooper.packetevents.protocol.entity.data.EntityData
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.protocol.player.GameMode
import com.github.retrooper.packetevents.protocol.player.UserProfile
import com.github.retrooper.packetevents.util.Vector3d
import com.github.retrooper.packetevents.wrapper.PacketWrapper
import com.github.retrooper.packetevents.wrapper.play.server.*
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.NpcPose
import dev.slne.surf.npc.api.npc.animation.NpcAnimationType
import dev.slne.surf.npc.bukkit.util.buildMetaData
import dev.slne.surf.npc.bukkit.util.buildNullInfo
import dev.slne.surf.npc.bukkit.util.emptyComponent
import dev.slne.surf.npc.bukkit.util.toEntityPose
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
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
            val yaw: Float,
            val pitch: Float
        ) : NpcPackets() {
            override fun build() = WrapperPlayServerSpawnEntity(
                entityId,
                uuid,
                EntityTypes.PLAYER,
                PacketLocation(Vector3d(location.x, location.y, location.z), yaw, pitch),
                yaw,
                0,
                null
            )
        }

        data class NpcMetaDataPacket(val entityId: Int, val skinParts: Byte = 0x7F.toByte()) :
            NpcPackets() {
            override fun build() = WrapperPlayServerEntityMetadata(
                entityId,
                listOf(
                    buildMetaData(16, EntityDataTypes.BYTE, skinParts),
                    buildMetaData(0, EntityDataTypes.BYTE, 0x02.toByte()),
                )
            )
        }

        data class NpcPoseChangePacket(val entityId: Int, val pose: NpcPose) : NpcPackets() {
            override fun build() = WrapperPlayServerEntityMetadata(
                entityId,
                listOf(EntityData(6, EntityDataTypes.ENTITY_POSE, pose.toEntityPose()))
            )
        }

        data class NpcAnimationPacket(val entityId: Int, val animation: NpcAnimationType) :
            NpcPackets() {
            override fun build() = WrapperPlayServerEntityAnimation(
                entityId,
                when (animation) {
                    NpcAnimationType.SWING_ARM_MAIN -> WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_MAIN_ARM
                    NpcAnimationType.SWING_ARM_OFF -> WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_OFF_HAND
                    NpcAnimationType.GET_DAMAGE -> WrapperPlayServerEntityAnimation.EntityAnimationType.HURT
                    NpcAnimationType.LEAVE_BED -> WrapperPlayServerEntityAnimation.EntityAnimationType.WAKE_UP
                    NpcAnimationType.HIT_CRITICAL -> WrapperPlayServerEntityAnimation.EntityAnimationType.CRITICAL_HIT
                    NpcAnimationType.HIT_MAGIC -> WrapperPlayServerEntityAnimation.EntityAnimationType.MAGIC_CRITICAL_HIT
                }
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

        data class NpcInfoAddPacket(
            val profile: UserProfile,
            val displayName: Component,
            val listed: Boolean = false
        ) : NpcPackets() {
            override fun build() = WrapperPlayServerPlayerInfoUpdate(
                WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER,
                WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                    profile,
                    listed,
                    0,
                    GameMode.SURVIVAL,
                    displayName,
                    null
                )
            )
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

