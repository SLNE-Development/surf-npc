package dev.slne.surf.npc.example.listener

import dev.slne.surf.npc.api.event.NpcCollisionEvent
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ExampleNpcListener : Listener {
    @EventHandler
    fun onNpcCollision(event: NpcCollisionEvent) {
        val npc = event.npc
        val player = event.player

        player.velocity = player.location.direction.multiply(-1.5)
    }

    @EventHandler
    fun onNpcInteract(event: NpcInteractEvent) {
        val npc = event.npc
        val player = event.player

        val displayName = npc.getPropertyValue(NpcProperty.Internal.DISPLAYNAME, Component::class) ?: return

        if(npc.hasProperty("example_npc")) {
            player.sendText {
                append(displayName)
                spacer(" ")
                info(" Hey, du hast die Property 'example_npc'! Diese wurde über das DSL erstellt.")
            }
        }

        player.sendText {
            spacer("[")
            append(displayName)
            spacer("]")
            info(" Hey, schön das du da bist! Dies ist ein NPC, welcher mit dem DSL erstellt wurde.")
        }
    }
}