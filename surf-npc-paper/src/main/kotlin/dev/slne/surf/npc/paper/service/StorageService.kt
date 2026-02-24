package dev.slne.surf.npc.paper.service

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.paper.config.NpcConfig
import dev.slne.surf.npc.paper.config.NpcPropertyConfig
import dev.slne.surf.npc.paper.controller.npcController
import dev.slne.surf.npc.paper.plugin
import dev.slne.surf.npc.paper.property.propertyTypeRegistry
import dev.slne.surf.surfapi.core.api.config.manager.SpongeConfigManager
import dev.slne.surf.surfapi.core.api.config.surfConfigApi
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.toMutableObjectSet
import java.nio.file.Files
import java.nio.file.Path

val npcStorageService = NpcStorageService()

class NpcStorageService {

    private val npcFolder get() = plugin.dataPath.resolve("npcs")
    private val configManagers =
        mutableObject2ObjectMapOf<String, SpongeConfigManager<NpcConfig>>()

    fun initialize() {
        Files.createDirectories(npcFolder)
    }

    private fun npcFile(uniqueName: String): Path =
        npcFolder.resolve("$uniqueName.yml")

    fun loadAll(): Int {
        Files.list(npcFolder)
            .filter { it.toString().endsWith(".yml") }
            .forEach { file ->
                val manager = surfConfigApi.createSpongeYmlConfigManager(
                    NpcConfig::class.java,
                    file.parent,
                    file.fileName.toString()
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
                    properties = mutableObject2ObjectMapOf()
                )

                config.properties.forEach { (key, prop) ->
                    val type = propertyTypeRegistry.get(prop.type) ?: return@forEach
                    val value = type.decode(prop.value)
                    npc.addProperty(NpcProperty(key, value, type))
                }

                npcController.registerNpc(npc)
                configManagers[npc.uniqueName] = manager
            }

        return npcController.npcs.size
    }

    fun save(npc: Npc) {
        if (npc.isPersistent()) {
            return
        }

        val file = npcFile(npc.uniqueName)

        val manager = configManagers.getOrPut(npc.uniqueName) {
            surfConfigApi.createSpongeYmlConfigManager(
                NpcConfig::class.java,
                file.parent,
                file.fileName.toString()
            )
        }

        manager.config.apply {
            id = npc.id
            entityType = npc.entityType
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
        npcController.npcs.forEach { save(it) }
        return npcController.npcs.size
    }

    fun delete(npc: Npc) {
        val file = npcFile(npc.uniqueName)
        configManagers.remove(npc.uniqueName)
        Files.deleteIfExists(file)
    }

    fun reload(): Int {
        npcController.npcs.forEach { it.delete() }
        configManagers.clear()
        return loadAll()
    }
}
