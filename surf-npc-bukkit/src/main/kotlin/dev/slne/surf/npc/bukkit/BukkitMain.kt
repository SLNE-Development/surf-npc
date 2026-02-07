package dev.slne.surf.npc.bukkit

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.bukkit.command.npcCommand
import dev.slne.surf.npc.bukkit.listener.ConnectionListener
import dev.slne.surf.npc.bukkit.listener.InternalNpcEventListener
import dev.slne.surf.npc.bukkit.listener.NpcListener
import dev.slne.surf.npc.bukkit.listener.WorldChangeListener
import dev.slne.surf.npc.bukkit.npc.property.impl.*
import dev.slne.surf.npc.bukkit.property.impl.*
import dev.slne.surf.npc.bukkit.property.propertyTypeRegistry
import dev.slne.surf.npc.bukkit.service.versionService
import dev.slne.surf.npc.core.service.storageService
import dev.slne.surf.surfapi.bukkit.api.event.register
import org.bukkit.plugin.java.JavaPlugin

class BukkitMain : SuspendingJavaPlugin() {
    override fun onEnable() {
        PacketEvents.getAPI().eventManager.registerListener(
            NpcListener(),
            PacketListenerPriority.NORMAL
        )

        ConnectionListener().register()
        WorldChangeListener().register()
        InternalNpcEventListener().register()

        propertyTypeRegistry.register(BooleanPropertyType(NpcPropertyType.Types.BOOLEAN_ID))
        propertyTypeRegistry.register(ComponentPropertyType(NpcPropertyType.Types.COMPONENT_ID))
        propertyTypeRegistry.register(FloatPropertyType(NpcPropertyType.Types.FLOAT_ID))
        propertyTypeRegistry.register(IntPropertyType(NpcPropertyType.Types.INT_ID))
        propertyTypeRegistry.register(LongPropertyType(NpcPropertyType.Types.LONG_ID))
        propertyTypeRegistry.register(StringPropertyType(NpcPropertyType.Types.STRING_ID))
        propertyTypeRegistry.register(DoublePropertyType(NpcPropertyType.Types.DOUBLE_ID))
        propertyTypeRegistry.register(LocationPropertyType(NpcPropertyType.Types.LOCATION_ID))
        propertyTypeRegistry.register(UuidPropertyType(NpcPropertyType.Types.UUID_ID))
        propertyTypeRegistry.register(NamedTextColorPropertyType(NpcPropertyType.Types.NAMED_TEXT_COLOR_ID))
        propertyTypeRegistry.register(NpcRotationPropertyType(NpcPropertyType.Types.NPC_ROTATION_ID))
        propertyTypeRegistry.register(SkinDataPropertyType(NpcPropertyType.Types.SKIN_DATA_ID))
        propertyTypeRegistry.register(NpcCreatorTypePropertyType(NpcPropertyType.Types.NPC_CREATOR_TYPE))

        storageService.initialize()
        storageService.loadNpcs()

        npcCommand()

        launch {
            versionService.fetchGithubVersion()
        }
    }

    override fun onDisable() {
        storageService.saveNpcs()
    }
}

val plugin get() = JavaPlugin.getPlugin(BukkitMain::class.java)