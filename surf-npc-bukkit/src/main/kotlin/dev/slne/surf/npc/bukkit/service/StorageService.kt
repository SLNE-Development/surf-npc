package dev.slne.surf.npc.bukkit.service

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.bukkit.config.NpcConfig
import dev.slne.surf.npc.bukkit.config.NpcPropertyConfig
import dev.slne.surf.npc.bukkit.controller.npcController
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.property.propertyTypeRegistry

import dev.slne.surf.surfapi.core.api.config.manager.SpongeConfigManager
import dev.slne.surf.surfapi.core.api.config.surfConfigApi
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.toMutableObjectSet
import java.nio.file.Files

val npcStorageService = NpcStorageService()

class NpcStorageService {

    private val npcFolder get() = plugin.dataPath.resolve("npcs")
    private val configManagers =
        mutableObject2ObjectMapOf<String, SpongeConfigManager<NpcConfig>>()

    fun initialize() {
        Files.createDirectories(npcFolder)
    }

    fun loadAll(): Int {
        Files.list(npcFolder).filter {
            Files.exists(it.resolve("npc.yml"))
        }.forEach { dir ->
            val manager = surfConfigApi.createSpongeYmlConfigManager(
                NpcConfig::class.java,
                dir,
                "npc.yml"
            )

            val config = manager.config

            val npc = Npc(
                id = config.id,
                npcUuid = config.npcUuid,
                entityType = config.entityType,
                nameTagId = config.nameTagId,
                nameTagUuid = config.nameTagUuid,
                uniqueName = config.uniqueName,
                npcSittingId = config.sittingId,
                npcSittingUuid = config.sittingUuid,
                viewers = if (config.viewerAmount == -1) null
                else config.viewers.toMutableObjectSet(),
                properties = mutableObject2ObjectMapOf<String, NpcProperty>()
            )

            config.properties.forEach { (key, prop) ->
                val type = propertyTypeRegistry.get(prop.type) ?: return@forEach
                val value = type.decode(prop.value)

                npc.addProperty(
                    NpcProperty(key, value, type)
                )
            }

            npcController.registerNpc(npc)
            configManagers[npc.uniqueName] = manager
        }

        return npcController.getNpcs().size
    }

    fun save(npc: Npc) {
        val dir = npcFolder.resolve(npc.uniqueName)
        Files.createDirectories(dir)

        val manager = configManagers.getOrPut(npc.uniqueName) {
            surfConfigApi.createSpongeYmlConfigManager(
                NpcConfig::class.java,
                dir,
                "npc.yml"
            )
        }

        manager.config.apply {
            id = npc.id
            npcUuid = npc.npcUuid
            nameTagId = npc.nameTagId
            nameTagUuid = npc.nameTagUuid
            uniqueName = npc.uniqueName
            sittingId = npc.npcSittingId
            sittingUuid = npc.npcSittingUuid
            viewerAmount = npc.viewers?.size ?: -1
            viewers = npc.viewers?.toList() ?: emptyList()
            properties = npc.properties.values.associate {
                it.key to NpcPropertyConfig(
                    type = it.type.id,
                    value = it.type.encode(it.value)
                )
            }
        }

        manager.save()
    }

    fun saveAll(): Int {
        npcController.getNpcs().forEach { save(it) }
        return npcController.getNpcs().size
    }

    fun delete(npc: Npc) {
        val dir = npcFolder.resolve(npc.uniqueName)
        configManagers.remove(npc.uniqueName)
        if (Files.exists(dir)) {
            dir.toFile().deleteRecursively()
        }
    }

    fun reload(): Int {
        npcController.getNpcs().forEach { it.delete() }
        configManagers.clear()
        return loadAll()
    }
}
