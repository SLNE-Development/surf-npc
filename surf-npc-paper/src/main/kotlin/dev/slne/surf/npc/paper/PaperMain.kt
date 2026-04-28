package dev.slne.surf.npc.paper

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.api.paper.SurfApiPaper
import dev.slne.surf.api.paper.event.register
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.paper.command.npcCommand
import dev.slne.surf.npc.paper.listener.*
import dev.slne.surf.npc.paper.property.impl.*
import dev.slne.surf.npc.paper.property.propertyTypeRegistry
import dev.slne.surf.npc.paper.service.npcStorageService
import dev.slne.surf.npc.paper.service.versionService
import org.bukkit.plugin.java.JavaPlugin

class PaperMain : SuspendingJavaPlugin() {
    override fun onEnable() {
        PacketEvents.getAPI().eventManager.registerListener(
            NpcListener(),
            PacketListenerPriority.NORMAL
        )

        ConnectionListener.register()
        WorldChangeListener.register()
        InternalNpcEventListener.register()

        if (SurfApiPaper.isCanvasMc) {
            FoliaAdditionsListener.register()
        }

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
        propertyTypeRegistry.register(SkinDataPropertyType(NpcPropertyType.Types.SKIN_DATA_ID))
        propertyTypeRegistry.register(RotationTypePropertyType(NpcPropertyType.Types.ROTATION_TYPE_ID))
        propertyTypeRegistry.register(NpcPosePropertyType(NpcPropertyType.Types.NPC_POSE_ID))

        npcStorageService.initialize()
        npcStorageService.loadAll()

        npcCommand()

        launch {
            versionService.fetchGithubVersion()
        }
    }

    override fun onDisable() {
        npcStorageService.saveAll()
    }
}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)