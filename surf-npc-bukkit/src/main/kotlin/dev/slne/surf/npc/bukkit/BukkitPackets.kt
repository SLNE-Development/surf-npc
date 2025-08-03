package dev.slne.surf.npc.bukkit

import com.github.retrooper.packetevents.protocol.entity.data.EntityData
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.protocol.player.GameMode
import com.github.retrooper.packetevents.protocol.player.UserProfile
import com.github.retrooper.packetevents.util.Vector3d
import com.github.retrooper.packetevents.wrapper.play.server.*
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import java.util.UUID

fun createPlayerInfoPacket(profile: UserProfile, displayName: Component, listed: Boolean = false) = WrapperPlayServerPlayerInfoUpdate(
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
fun createEntityMetadataPacket(npcEntityId: Int) = WrapperPlayServerEntityMetadata(
    npcEntityId,
    listOf(
        EntityData(17, EntityDataTypes.BYTE, 0x7F.toByte()),
        EntityData(0, EntityDataTypes.BYTE, 0x02.toByte()),
    )
)
fun createPlayerSpawnPacket(
    entityId: Int,
    uuid: UUID,
    location: Location,
    yaw: Float,
    pitch: Float
) = WrapperPlayServerSpawnEntity(
    entityId,
    uuid,
    EntityTypes.PLAYER,
    com.github.retrooper.packetevents.protocol.world.Location(Vector3d(location.x, location.y, location.z), yaw, pitch),
    yaw,
    0,
    null
)
fun createNametagSpawnPacket(
    entityId: Int,
    uuid: UUID,
    location: Location
) = WrapperPlayServerSpawnEntity(
    entityId,
    uuid,
    EntityTypes.TEXT_DISPLAY,
    com.github.retrooper.packetevents.protocol.world.Location(Vector3d(location.x, location.y + 2, location.z), 0f, 0f),
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
fun createTeleportPacket(entityId: Int, location: Location, onGround: Boolean = false) = WrapperPlayServerEntityTeleport(
    entityId,
    SpigotConversionUtil.fromBukkitLocation(location),
    onGround
)
