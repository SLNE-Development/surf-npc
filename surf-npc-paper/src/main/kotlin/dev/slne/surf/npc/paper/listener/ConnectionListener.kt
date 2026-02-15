package dev.slne.surf.npc.paper.listener

import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.paper.controller.npcController
import dev.slne.surf.npc.paper.service.versionService
import dev.slne.surf.npc.paper.util.PermissionRegistry
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.clickOpensUrl
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class ConnectionListener : Listener {
    @EventHandler
    fun onConnect(event: PlayerJoinEvent) {
        val player = event.player

        npcController.npcs
            .filter {
                val viewers = it.viewers
                viewers == null || viewers.contains(player.uniqueId)
            }
            .filter {
                val npcLocation =
                    it.getPropertyValue(NpcProperty.Internal.LOCATION, Location::class)
                npcLocation == null || npcLocation.world.uid == player.world.uid
            }
            .forEach {
                npcController.showToViewer(it, player.uniqueId)
            }

        if (player.hasPermission(PermissionRegistry.UPDATE_NOTIFY)) {
            if (versionService.isUpToDate()) {
                return
            }

            versionService.latestVersion?.let {
                player.sendText {
                    appendInfoPrefix()
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
                    appendInfoPrefix()
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