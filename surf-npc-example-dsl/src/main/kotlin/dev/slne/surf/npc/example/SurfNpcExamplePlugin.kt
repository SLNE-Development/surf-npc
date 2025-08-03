package dev.slne.surf.npc.example

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.dsl.npcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.npc.example.listener.ExampleNpcListener
import dev.slne.surf.surfapi.core.api.util.logger
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit

class SurfNpcExamplePlugin(): SuspendingJavaPlugin() {
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(ExampleNpcListener(), this)

        /**
         * Creates an example NPC using the DSL provided by the Surf-NPC API.
         */
        npc {
            displayName = MiniMessage.miniMessage().deserialize("<rainbow>Example Npc by surf-npc-example")
            uniqueName = "example_npc"

            /**
             * Skin data can be created using the DSL or by using the SurfNpcApi#getSkin function.
             */
            skin = dev.slne.surf.npc.api.dsl.skin {
                ownerName = "CastCrafter"
                value = "SkinValue"
                signature = "SkinSignature"
            }

            /**
             * Location can be created using the DSL.
             */
            location = dev.slne.surf.npc.api.dsl.location {
                world = "world"
                x = 0.0
                y = 0.0
                z = 0.0
            }
            global = true
            rotationType = NpcRotationType.PER_PLAYER
        }

        /**
         * Retrieves the created NPC using the SurfNpcApi.
         * If the NPC is not found, it logs a warning message.
         */
        val npc = surfNpcApi.getNpc("example_npc") ?: return run {
            logger().atWarning().log("Failed to create example NPC: NPC not found after creation.")
        }

        /**
         * Adds a property to the NPC using the DSL.
         * This property is of type Boolean, which is registered in the surf-npc Api.
         */
        surfNpcApi.addProperty(npc, npcProperty {
            key = "example_npc"
            value = true
            type = surfNpcApi.getPropertyType(NpcPropertyType.Types.BOOLEAN) ?: return@npcProperty run {
                logger().atWarning().log("Failed to create example NPC: Boolean property type not found.")
            }
        })
    }
}