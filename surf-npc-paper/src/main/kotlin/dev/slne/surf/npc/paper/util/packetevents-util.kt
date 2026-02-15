package dev.slne.surf.npc.paper.util

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.entity.data.EntityData
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType
import com.github.retrooper.packetevents.protocol.player.User
import com.github.retrooper.packetevents.wrapper.PacketWrapper
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams
import org.bukkit.Bukkit
import org.bukkit.entity.Player
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
    PacketEvents.getAPI().playerManager.getUser(Bukkit.getPlayer(uuid) ?: return).sendPacket(this)
}

fun PacketWrapper<*>.sendPacket(player: Player) {
    PacketEvents.getAPI().playerManager.getUser(player).sendPacket(this)
}


fun buildNullInfo(): WrapperPlayServerTeams.ScoreBoardTeamInfo? = null