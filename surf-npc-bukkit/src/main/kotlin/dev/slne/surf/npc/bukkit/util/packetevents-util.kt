package dev.slne.surf.npc.bukkit.util

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.player.User
import org.bukkit.Bukkit
import java.util.UUID

fun UUID.toUser(): User? {
    return PacketEvents.getAPI().playerManager.getUser(Bukkit.getPlayer(this) ?: return null)
}