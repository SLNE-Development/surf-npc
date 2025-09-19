package dev.slne.surf.npc.bukkit

import com.github.retrooper.packetevents.protocol.entity.data.EntityData
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.protocol.player.GameMode
import com.github.retrooper.packetevents.protocol.player.UserProfile
import com.github.retrooper.packetevents.util.Vector3d
import com.github.retrooper.packetevents.wrapper.play.server.*
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.NpcPose
import dev.slne.surf.npc.api.npc.animation.NpcAnimationType
import dev.slne.surf.npc.bukkit.util.toEntityPose
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.*
import com.github.retrooper.packetevents.protocol.world.Location as PacketLocation
import org.bukkit.Location as BukkitLocation

fun createPlayerInfoPacket(profile: UserProfile, displayName: Component, listed: Boolean = false) =
    WrapperPlayServerPlayerInfoUpdate(
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

fun createEntityMetadataPacket(npcEntityId: Int, skinParts: Byte = 0x7F.toByte()) =
    WrapperPlayServerEntityMetadata(
        npcEntityId,
        listOf(
            EntityData(17, EntityDataTypes.BYTE, skinParts),
            EntityData(0, EntityDataTypes.BYTE, 0x02.toByte()),
        )
    )

fun createSpawnSittingArmorStandPacket(npc: Npc, npcLocation: BukkitLocation) =
    WrapperPlayServerSpawnEntity(
        npc.npcSittingId,
        npc.npcSittingUuid,
        EntityTypes.ARMOR_STAND,
        SpigotConversionUtil.fromBukkitLocation(npcLocation.clone().subtract(0.0, 2.0, 0.0)),
        0f,
        0,
        null
    )

fun createSittingArmorStandMetadataPacket(npc: Npc) = WrapperPlayServerEntityMetadata(
    npc.npcSittingId,
    listOf(
        EntityData(0, EntityDataTypes.BYTE, 0x20.toByte()),
    )
)

fun createMountSittingArmorStandPacket(npc: Npc) = WrapperPlayServerSetPassengers(
    npc.npcSittingId,
    intArrayOf(npc.id)
)

fun createDestroySittingArmorStandPacket(npc: Npc) =
    WrapperPlayServerDestroyEntities(npc.npcSittingId)

fun createPoseChangePacket(npcEntityId: Int, pose: NpcPose) = WrapperPlayServerEntityMetadata(
    npcEntityId,
    listOf(
        EntityData(
            6,
            EntityDataTypes.ENTITY_POSE,
            pose.toEntityPose()
        )
    )
)

fun createPlayerSpawnPacket(
    entityId: Int,
    uuid: UUID,
    location: BukkitLocation,
    yaw: Float,
    pitch: Float
) = WrapperPlayServerSpawnEntity(
    entityId,
    uuid,
    EntityTypes.PLAYER,
    PacketLocation(
        Vector3d(
            location.x,
            location.y,
            location.z
        ), yaw, pitch
    ),
    yaw,
    0,
    null
)

fun createNametagSpawnPacket(
    entityId: Int,
    uuid: UUID,
    location: BukkitLocation
) = WrapperPlayServerSpawnEntity(
    entityId,
    uuid,
    EntityTypes.TEXT_DISPLAY,
    PacketLocation(
        Vector3d(
            location.x,
            location.y + 2,
            location.z
        ), 0f, 0f
    ),
    0f,
    0,
    null
)

fun createNametagMetadataPacket(
    entityId: Int,
    displayName: Component
) = WrapperPlayServerEntityMetadata(
    entityId,
    listOf(
        EntityData(23, EntityDataTypes.ADV_COMPONENT, displayName),
        EntityData(15, EntityDataTypes.BYTE, 3.toByte()),
        EntityData(27, EntityDataTypes.BYTE, 0x02.toByte())
    )
)

fun createTeamCreatePacket(
    teamName: String,
    displayName: Component
) = WrapperPlayServerTeams(
    teamName,
    WrapperPlayServerTeams.TeamMode.CREATE,
    WrapperPlayServerTeams.ScoreBoardTeamInfo(
        displayName,
        Component.empty(),
        Component.empty(),
        WrapperPlayServerTeams.NameTagVisibility.NEVER,
        WrapperPlayServerTeams.CollisionRule.ALWAYS,
        NamedTextColor.RED,
        WrapperPlayServerTeams.OptionData.NONE
    )
)

private val nullInfo: WrapperPlayServerTeams.ScoreBoardTeamInfo? = null

fun createTeamAddEntityPacket(teamName: String, entityUuid: String) = WrapperPlayServerTeams(
    teamName,
    WrapperPlayServerTeams.TeamMode.ADD_ENTITIES,
    nullInfo,
    entityUuid
)

fun createDestroyPacket(vararg entityIds: Int) = WrapperPlayServerDestroyEntities(*entityIds)
fun createPlayerInfoRemovePacket(npcUuid: UUID) = WrapperPlayServerPlayerInfoRemove(npcUuid)
fun createRotationPackets(entityId: Int, yaw: Float, pitch: Float) = Pair(
    WrapperPlayServerEntityRotation(entityId, yaw, pitch, true),
    WrapperPlayServerEntityHeadLook(entityId, yaw)
)

fun createTeleportPacket(entityId: Int, location: BukkitLocation, onGround: Boolean = false) =
    WrapperPlayServerEntityTeleport(
        entityId,
        SpigotConversionUtil.fromBukkitLocation(location),
        onGround
    )

fun createEntityAnimation(entityId: Int, animation: NpcAnimationType) =
    WrapperPlayServerEntityAnimation(
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
