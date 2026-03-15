package dev.slne.surf.npc.paper.util

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.entity.data.EntityData
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType
import com.github.retrooper.packetevents.protocol.player.User
import com.github.retrooper.packetevents.wrapper.PacketWrapper
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams
import org.bukkit.Bukkit
import java.util.*

fun UUID.toUser(): User? {
    return PacketEvents.getAPI().playerManager.getUser(Bukkit.getPlayer(this) ?: return null)
}

fun <T> buildMetaData(index: Int, type: EntityDataType<T>, value: T) = EntityData(
    index,
    type,
    value
)

fun PacketWrapper<*>.sendPacket(uuid: UUID) {
    val player = Bukkit.getPlayer(uuid) ?: return
    val packetPlayer = PacketEvents.getAPI().playerManager.getUser(player) ?: return

    packetPlayer.sendPacket(this)
}


fun buildNullInfo(): WrapperPlayServerTeams.ScoreBoardTeamInfo? = null