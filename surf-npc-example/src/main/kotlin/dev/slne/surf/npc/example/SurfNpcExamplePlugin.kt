package dev.slne.surf.npc.example

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.npc.api.util.addEventHandler
import dev.slne.surf.npc.example.listener.ExampleNpcListener
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.EntityType

class SurfNpcExamplePlugin() : SuspendingJavaPlugin() {
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(ExampleNpcListener(), this)

        val npc = surfNpcApi.createNpc(
            displayName = MiniMessage.miniMessage()
                .deserialize("<rainbow>Example Npc by surf-npc-example"),
            uniqueName = "example_npc",
            type = EntityType.MANNEQUIN,
            location = Location(Bukkit.getWorlds().first(), 0.0, 0.0, 0.0),
            skin = NpcSkin.empty()
        )

        surfNpcApi.addProperty(
            npc, NpcProperty(
                "example_npc",
                true,
                NpcPropertyType.Types.BOOLEAN_TYPE
            )
        )

        npc.addEventHandler<NpcInteractEvent> {
            it.player.sendText {
                appendInfoPrefix()
                spacer("[")
                variableKey(npc.uniqueName)
                spacer("]")
                appendSpace()
                spacer("Ich bin ein Beispiel Npc. Diese Reaktion wurde mithilfe des DSL-Event Handlers erstellt.")
            }
        }
    }
}