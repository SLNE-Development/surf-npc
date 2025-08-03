package dev.slne.surf.npc.bukkit.storage

import com.google.auto.service.AutoService
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.bukkit.npc.BukkitNpc
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.property.BukkitNpcProperty
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.npc.core.service.StorageService
import dev.slne.surf.surfapi.core.api.util.logger
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.toMutableObjectSet
import net.kyori.adventure.util.Services
import org.bukkit.configuration.file.YamlConfiguration
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

@AutoService(StorageService::class)
class BukkitStorageService : StorageService, Services.Fallback {
    private val npcFolder: Path
        get() = plugin.dataPath.resolve("npcs")

    override fun initialize() {
        if (!Files.exists(npcFolder)) {
            Files.createDirectories(npcFolder)
        }
    }

    override fun loadNpcs(): Int {
        val files = Files.list(npcFolder).filter { it.toString().endsWith(".yml") }.toList()

        files.forEach { path ->
            val config = YamlConfiguration.loadConfiguration(path.toFile())
            try {
                val id = config.getInt("npc.data.id")
                val uuid = UUID.fromString(config.getString("npc.data.uuid"))
                val nameTagId = config.getInt("npc.data.nameTagId")
                val nameTagUuid = UUID.fromString(
                    config.getString("npc.data.nameTagUuid")
                        ?: error("NameTag UUID is missing in NPC config file: ${path.fileName}")
                )
                val uniqueName = config.getString("npc.data.uniqueName")
                    ?: error("Unique Name is missing in NPC config file: ${path.fileName}")

                val viewers = config.getStringList("npc.data.viewers").map(UUID::fromString)
                    .toMutableObjectSet()

                val npc = BukkitNpc(
                    id = id,
                    npcUuid = uuid,
                    nameTagId = nameTagId,
                    nameTagUuid = nameTagUuid,
                    viewers = viewers,
                    properties = mutableObject2ObjectMapOf<String, NpcProperty>(),
                    uniqueName = uniqueName
                )

                val propsSection =
                    config.getConfigurationSection("npc.data.properties") ?: return@forEach
                val sectionKeys = propsSection.getKeys(false)

                sectionKeys.forEach { key ->
                    val valueString =
                        config.getString("npc.data.properties.$key.value") ?: return@forEach
                    val typeString =
                        config.getString("npc.data.properties.$key.type") ?: return@forEach

                    val type = propertyTypeRegistry.get(typeString)
                        ?: error("Unknown property type: $typeString")
                    val value = type.decode(valueString)

                    npc.addProperty(
                        BukkitNpcProperty(
                            key, value, type
                        )
                    )
                }

                npcController.registerNpc(npc)
            } catch (e: Exception) {
                logger().atWarning()
                    .log("Failed to load NPC from file ${path.fileName}: ${e.message}")
            }
        }

        logger().atInfo()
            .log("Successfully loaded ${npcController.getNpcs().size} NPCs from ${files.size} files!")

        return npcController.getNpcs().size
    }

    override fun saveNpcs(): Int {
        Files.list(npcFolder)
            .filter { it.toString().endsWith(".yml") }
            .forEach(Files::delete)

        npcController.getNpcs().forEach { npc ->
            val file = npcFolder.resolve("${npc.uniqueName}.yml").toFile()
            val config = YamlConfiguration()

            config["npc.data.id"] = npc.id
            config["npc.data.uuid"] = npc.npcUuid.toString()
            config["npc.data.nameTagId"] = npc.nameTagId
            config["npc.data.nameTagUuid"] = npc.nameTagUuid.toString()
            config["npc.data.uniqueName"] = npc.uniqueName

            config["npc.data.viewers"] = npc.viewers.map(UUID::toString)

            npc.properties.values.forEach {
                val path = "npc.data.properties.${it.key}"
                config["$path.value"] = it.type.encode(it.value)
                config["$path.type"] = it.type.id
            }

            config.save(file)
        }

        logger().atInfo().log("Successfully saved ${npcController.getNpcs().size} NPCs to files!")
        return npcController.getNpcs().size
    }

    override fun import(fileName: String): Boolean {
        val file = npcFolder.resolve("$fileName.yml").toFile()

        if (!file.exists()) {
            logger().atWarning().log("NPC file $fileName does not exist in the NPC folder.")
            return false
        }

        val config = YamlConfiguration.loadConfiguration(file)
        try {
            val uniqueName = config.getString("npc.data.uniqueName")
                ?: error("Unique name is missing in NPC config file: $fileName.yml")

            if (npcController.getNpc(uniqueName) != null) {
                logger().atWarning()
                    .log("NPC with unique name '$uniqueName' already exists. Skipping import.")
                return false
            }

            val id = config.getInt("npc.data.id")
            val uuid = UUID.fromString(config.getString("npc.data.uuid"))
            val nameTagId = config.getInt("npc.data.nameTagId")
            val nameTagUuid = UUID.fromString(
                config.getString("npc.data.nameTagUuid")
                    ?: error("NameTag UUID is missing in NPC config file: $fileName.yml")
            )

            val viewers =
                config.getStringList("npc.data.viewers").map(UUID::fromString).toMutableObjectSet()

            val npc = BukkitNpc(
                id = id,
                npcUuid = uuid,
                nameTagId = nameTagId,
                nameTagUuid = nameTagUuid,
                viewers = viewers,
                properties = mutableObject2ObjectMapOf<String, NpcProperty>(),
                uniqueName = uniqueName
            )

            val propsSection = config.getConfigurationSection("npc.data.properties") ?: return false
            val sectionKeys = propsSection.getKeys(false)

            sectionKeys.forEach { key ->
                val valueString =
                    config.getString("npc.data.properties.$key.value") ?: return@forEach
                val typeString = config.getString("npc.data.properties.$key.type") ?: return@forEach

                val type = propertyTypeRegistry.get(typeString)
                    ?: error("Unknown property type: $typeString")
                val value = type.decode(valueString)

                npc.addProperty(
                    BukkitNpcProperty(
                        key, value, type
                    )
                )
            }

            npcController.registerNpc(npc)
            npc.show()
        } catch (e: Exception) {
            logger().atWarning().log("Failed to import NPC from file $fileName.yml: ${e.message}")
            return false
        }

        return true
    }

    override fun export(npc: Npc) {
        val file = npcFolder.resolve("${npc.uniqueName}.yml").toFile()
        val config = YamlConfiguration()

        config["npc.data.id"] = npc.id
        config["npc.data.uuid"] = npc.npcUuid.toString()
        config["npc.data.nameTagId"] = npc.nameTagId
        config["npc.data.nameTagUuid"] = npc.nameTagUuid.toString()
        config["npc.data.uniqueName"] = npc.uniqueName

        config["npc.data.viewers"] = npc.viewers.map(UUID::toString)

        npc.properties.values.forEach {
            val path = "npc.data.properties.${it.key}"
            config["$path.value"] = it.type.encode(it.value)
            config["$path.type"] = it.type.id
        }

        config.save(file)
    }

    override fun importAll(): Int {
        val existingNames = npcController.getNpcs().map { it.uniqueName }.toSet()
        val files = Files.list(npcFolder).filter { it.toString().endsWith(".yml") }.toList()
        var importedCount = 0

        files.forEach { path ->
            val config = YamlConfiguration.loadConfiguration(path.toFile())

            try {
                val uniqueName = config.getString("npc.data.uniqueName")
                    ?: error("Unique name is missing in NPC config file: ${path.fileName}")

                if (existingNames.contains(uniqueName)) {
                    return@forEach
                }

                val id = config.getInt("npc.data.id")
                val uuid = UUID.fromString(config.getString("npc.data.uuid"))
                val nameTagId = config.getInt("npc.data.nameTagId")
                val nameTagUuid = UUID.fromString(
                    config.getString("npc.data.nameTagUuid")
                        ?: error("NameTag UUID is missing in NPC config file: ${path.fileName}")
                )

                val viewers = config.getStringList("npc.data.viewers").map(UUID::fromString)
                    .toMutableObjectSet()

                val npc = BukkitNpc(
                    id = id,
                    npcUuid = uuid,
                    nameTagId = nameTagId,
                    nameTagUuid = nameTagUuid,
                    viewers = viewers,
                    properties = mutableObject2ObjectMapOf<String, NpcProperty>(),
                    uniqueName = uniqueName
                )

                val propsSection =
                    config.getConfigurationSection("npc.data.properties") ?: return@forEach
                val sectionKeys = propsSection.getKeys(false)

                sectionKeys.forEach { key ->
                    val valueString =
                        config.getString("npc.data.properties.$key.value") ?: return@forEach
                    val typeString =
                        config.getString("npc.data.properties.$key.type") ?: return@forEach

                    val type = propertyTypeRegistry.get(typeString)
                        ?: error("Unknown property type: $typeString")
                    val value = type.decode(valueString)

                    npc.addProperty(
                        BukkitNpcProperty(
                            key, value, type
                        )
                    )
                }

                npcController.registerNpc(npc)
                npc.show()
                importedCount++
            } catch (e: Exception) {
                logger().atWarning()
                    .log("Failed to import NPC from file ${path.fileName}: ${e.message}")
            }
        }
        return importedCount
    }


    override fun exportAll(): Int {
        Files.list(npcFolder)
            .filter { it.toString().endsWith(".yml") }
            .forEach(Files::delete)

        npcController.getNpcs().forEach { npc ->
            val file = npcFolder.resolve("${npc.uniqueName}.yml").toFile()
            val config = YamlConfiguration()

            config["npc.data.id"] = npc.id
            config["npc.data.uuid"] = npc.npcUuid.toString()
            config["npc.data.nameTagId"] = npc.nameTagId
            config["npc.data.nameTagUuid"] = npc.nameTagUuid.toString()
            config["npc.data.uniqueName"] = npc.uniqueName

            config["npc.data.viewers"] = npc.viewers.map(UUID::toString)

            npc.properties.values.forEach {
                val path = "npc.data.properties.${it.key}"
                config["$path.value"] = it.type.encode(it.value)
                config["$path.type"] = it.type.id
            }

            config.save(file)
        }

        return npcController.getNpcs().size
    }

    override fun reloadFromDisk(): Int {
        npcController.despawnAllNpcs()
        npcController.getNpcs().forEach { it.delete() }

        return loadNpcs()
    }

    override fun saveToDisk(): Int {
        return saveNpcs()
    }
}
