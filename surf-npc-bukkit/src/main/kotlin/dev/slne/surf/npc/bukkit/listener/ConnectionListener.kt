package dev.slne.surf.npc.bukkit.listener

import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.bukkit.service.versionService
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.clickOpensUrl
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class ConnectionListener : Listener {
    @EventHandler
    fun onConnect(event: PlayerJoinEvent) {
        val player = event.player

        npcController.getNpcs()
            .filter {
                val viewers = it.viewers
                viewers == null || viewers.contains(player.uniqueId)
            }
            .filter {
                val npcLocation =
                    it.getPropertyValue(NpcProperty.Internal.LOCATION, NpcLocation::class)
                npcLocation == null || npcLocation.world == player.world.name
            }
            .forEach {
                it.spawn(player.uniqueId)
            }

        if (player.hasPermission(PermissionRegistry.UPDATE_NOTIFY)) {
            if (versionService.isUpToDate()) {
                return
            }

            versionService.latestVersion?.let {
                player.sendText {
                    appendPrefix()
                    info("Es ist eine")
                    appendSpace()
                    variableValue("neue Version")
                    appendSpace()
                    info("von")
                    appendSpace()
                    variableValue("surf-npc")
                    appendSpace()
                    info("verfügbar!")
                    appendNewline()
                    appendPrefix()
                    spacer(
                        "Klicke hier, um den neusten Release herunterzuladen.".toSmallCaps()
                    )
                    clickOpensUrl(
                        versionService.link
                            ?: "http://github.com/SLNE-Development/surf-npc/releases/latest"
                    )
                }
            }
        }
    }
}