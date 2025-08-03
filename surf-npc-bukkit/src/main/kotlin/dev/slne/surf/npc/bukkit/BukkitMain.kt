package dev.slne.surf.npc.bukkit

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.npc.api.npc.property.NpcPropertyType

import dev.slne.surf.npc.bukkit.command.NpcCommand
import dev.slne.surf.npc.bukkit.listener.ConnectionListener
import dev.slne.surf.npc.bukkit.listener.NpcListener
import dev.slne.surf.npc.bukkit.property.impl.BooleanPropertyType
import dev.slne.surf.npc.bukkit.property.impl.ComponentPropertyType
import dev.slne.surf.npc.bukkit.property.impl.DoublePropertyType
import dev.slne.surf.npc.bukkit.property.impl.FloatPropertyType
import dev.slne.surf.npc.bukkit.property.impl.IntPropertyType
import dev.slne.surf.npc.bukkit.npc.property.impl.NpcLocationPropertyType
import dev.slne.surf.npc.bukkit.property.impl.LongPropertyType
import dev.slne.surf.npc.bukkit.property.impl.NamedTextColorPropertyType
import dev.slne.surf.npc.bukkit.property.impl.NpcRotationPropertyType
import dev.slne.surf.npc.bukkit.property.impl.StringPropertyType
import dev.slne.surf.npc.bukkit.property.impl.UuidPropertyType
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.npc.core.service.storageService

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class BukkitMain : SuspendingJavaPlugin() {
    override fun onEnable() {
        PacketEvents.getAPI().eventManager.registerListener(NpcListener(), PacketListenerPriority.NORMAL)
        Bukkit.getPluginManager().registerEvents(ConnectionListener(), this)

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

        storageService.initialize()
        storageService.loadNpcs()

        NpcCommand("npc").register()
    }

    override fun onDisable() {
        storageService.saveNpcs()
    }
}

val plugin get() = JavaPlugin.getPlugin(BukkitMain::class.java)