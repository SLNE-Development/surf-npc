package dev.slne.surf.npc.bukkit

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.bukkit.command.NpcCommand
import dev.slne.surf.npc.bukkit.listener.ConnectionListener
import dev.slne.surf.npc.bukkit.listener.InternalNpcEventListener
import dev.slne.surf.npc.bukkit.listener.NpcListener
import dev.slne.surf.npc.bukkit.listener.WorldChangeListener
import dev.slne.surf.npc.bukkit.npc.property.impl.*
import dev.slne.surf.npc.bukkit.service.versionService
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.npc.core.service.storageService
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surf.surfapi.bukkit.api.metrics.Metrics
import org.bukkit.plugin.java.JavaPlugin

class BukkitMain : SuspendingJavaPlugin() {
    private lateinit var metrics: Metrics

    override fun onEnable() {
        PacketEvents.getAPI().eventManager.registerListener(
            NpcListener(),
            PacketListenerPriority.NORMAL
        )

        ConnectionListener().register()
        WorldChangeListener().register()
        InternalNpcEventListener().register()

        metrics = Metrics(this, 27049)

        propertyTypeRegistry.register(BooleanPropertyType(NpcPropertyType.Types.BOOLEAN))
        propertyTypeRegistry.register(ComponentPropertyType(NpcPropertyType.Types.COMPONENT))
        propertyTypeRegistry.register(FloatPropertyType(NpcPropertyType.Types.FLOAT))
        propertyTypeRegistry.register(IntPropertyType(NpcPropertyType.Types.INT))
        propertyTypeRegistry.register(LongPropertyType(NpcPropertyType.Types.LONG))
        propertyTypeRegistry.register(StringPropertyType(NpcPropertyType.Types.STRING))
        propertyTypeRegistry.register(DoublePropertyType(NpcPropertyType.Types.DOUBLE))
        propertyTypeRegistry.register(NpcLocationPropertyType(NpcPropertyType.Types.NPC_LOCATION))
        propertyTypeRegistry.register(UuidPropertyType(NpcPropertyType.Types.UUID))
        propertyTypeRegistry.register(NamedTextColorPropertyType(NpcPropertyType.Types.NAMED_TEXT_COLOR))
        propertyTypeRegistry.register(NpcRotationPropertyType(NpcPropertyType.Types.NPC_ROTATION))
        propertyTypeRegistry.register(SkinDataPropertyType(NpcPropertyType.Types.SKIN_DATA))
        propertyTypeRegistry.register(NpcCreatorTypePropertyType(NpcPropertyType.Types.NPC_CREATOR_TYPE))

        storageService.initialize()
        storageService.loadNpcs()

        NpcCommand("npc").register()
    }

    override suspend fun onEnableAsync() {
        versionService.fetchGithubVersion()
    }

    override fun onDisable() {
        if (::metrics.isInitialized) {
            metrics.shutdown()
        }

        storageService.saveNpcs()
    }
}

val plugin get() = JavaPlugin.getPlugin(BukkitMain::class.java)