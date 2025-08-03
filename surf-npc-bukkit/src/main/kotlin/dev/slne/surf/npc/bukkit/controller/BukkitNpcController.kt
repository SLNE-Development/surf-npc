package dev.slne.surf.npc.bukkit.controller

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.google.auto.service.AutoService
import dev.slne.surf.npc.api.event.NpcCreateEvent
import dev.slne.surf.npc.api.event.NpcDeleteEvent
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.result.NpcDeletionResult
import dev.slne.surf.npc.api.result.NpcRespawnResult
import dev.slne.surf.npc.api.result.NpcSpawnResult
import dev.slne.surf.npc.api.npc.rotation.NpcRotation
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.bukkit.npc.BukkitNpc
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.property.BukkitNpcProperty
import dev.slne.surf.npc.core.controller.NpcController
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.surfapi.bukkit.api.util.forEachPlayer
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.random
import dev.slne.surf.surfapi.core.api.util.toObjectList
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import net.kyori.adventure.util.Services
import java.util.UUID

@AutoService(NpcController::class)
class BukkitNpcController : NpcController, Services.Fallback {
    val npcs = mutableObjectSetOf<Npc>()

    override fun createNpc(
        uniqueName: String,
        displayName: Component,
        skinData: NpcSkin,
        location: NpcLocation,
        rotationType: NpcRotationType,
        rotation: NpcRotation,
        global: Boolean
    ): NpcCreationResult {
        val id = random.nextInt()
        val nameTagId = random.nextInt()
        val uuid = UUID.randomUUID()
        val nameTagUuid = UUID.randomUUID()

        if(this.getNpc(id) != null) {
            return NpcCreationResult.FAILED_ALREADY_EXISTS
        }

        if(this.getNpc(uniqueName) != null) {
            return NpcCreationResult.FAILED_ALREADY_EXISTS
        }

        val npc = BukkitNpc (
            id,
            mutableObject2ObjectMapOf<String, NpcProperty>(),
            mutableObjectSetOf<UUID>(),
            uuid,
            nameTagId,
            nameTagUuid,
            uniqueName
        )

        npc.addProperty(BukkitNpcProperty(
            NpcProperty.Internal.DISPLAYNAME, displayName, propertyTypeRegistry.get(
                NpcPropertyType.Types.COMPONENT) ?: error("Component property type not found")
        ))
        npc.addProperty(BukkitNpcProperty(
            NpcProperty.Internal.SKIN_OWNER, skinData.ownerName, propertyTypeRegistry.get(
                NpcPropertyType.Types.STRING) ?: error("STRING property type not found")
        ))
        npc.addProperty(BukkitNpcProperty(
            NpcProperty.Internal.SKIN_TEXTURE, skinData.value, propertyTypeRegistry.get(
                NpcPropertyType.Types.STRING) ?: error("STRING property type not found")
        ))
        npc.addProperty(BukkitNpcProperty(
            NpcProperty.Internal.SKIN_SIGNATURE, skinData.signature, propertyTypeRegistry.get(
                NpcPropertyType.Types.STRING) ?: error("STRING property type not found")
        ))
        npc.addProperty(BukkitNpcProperty(
            NpcProperty.Internal.LOCATION, location, propertyTypeRegistry.get(
                NpcPropertyType.Types.NPC_LOCATION) ?: error("LOCATION property type not found")
        ))
        npc.addProperty(BukkitNpcProperty(
            NpcProperty.Internal.ROTATION_TYPE, rotationType == NpcRotationType.PER_PLAYER, propertyTypeRegistry.get(
                NpcPropertyType.Types.BOOLEAN) ?: error("BOOLEAN property type not found")
        ))
        npc.addProperty(BukkitNpcProperty(
            NpcProperty.Internal.ROTATION_FIXED, rotation, propertyTypeRegistry.get(
                NpcPropertyType.Types.NPC_ROTATION) ?: error("ROTATION property type not found")
        ))
        npc.addProperty(BukkitNpcProperty(
            NpcProperty.Internal.VISIBILITY_GLOBAL, global, propertyTypeRegistry.get(
                NpcPropertyType.Types.BOOLEAN) ?: error("BOOLEAN property type not found")
        ))

        this.registerNpc(npc)

        if(global) {
            forEachPlayer {
                npc.spawn(it.uniqueId)
            }
        }

        plugin.launch(plugin.globalRegionDispatcher) {
            NpcCreateEvent(npc).callEvent()
        }

        return NpcCreationResult.SUCCESS
    }

    override fun deleteNpc(npc: Npc): NpcDeletionResult {
        if(!this.unregisterNpc(npc)) {
            return NpcDeletionResult.FAILED_NOT_FOUND
        }

        val global = npc.getPropertyValue(NpcProperty.Internal.VISIBILITY_GLOBAL, Boolean::class) ?: false

        if(global) {
            forEachPlayer { player ->
                npc.despawn(player.uniqueId)
            }
        } else {
            npc.viewers.forEach { viewer ->
                npc.despawn(viewer)
            }
        }

        npc.clearProperties()
        npc.viewers.clear()

        plugin.launch(plugin.globalRegionDispatcher) {
            NpcDeleteEvent(npc).callEvent()
        }

        return NpcDeletionResult.SUCCESS
    }

    override fun registerNpc(npc: Npc) {
        npcs.add(npc)
    }

    override fun unregisterNpc(npc: Npc): Boolean {
        return npcs.remove(npc)
    }

    override fun showNpc(
        npc: Npc,
        uuid: UUID
    ): NpcSpawnResult {
        if(!npcs.contains(npc)) {
            return NpcSpawnResult.FAILED_NOT_EXIST
        }

        if(npc.viewers.contains(uuid)) {
            return NpcSpawnResult.FAILED_ALREADY_SPAWNED
        }

        npc.spawn(uuid)

        return NpcSpawnResult.SUCCESS
    }

    override fun hideNpc(
        npc: Npc,
        uuid: UUID
    ): NpcDeletionResult {
        if(!npcs.contains(npc)) {
            return NpcDeletionResult.FAILED_NOT_FOUND
        }

        if(!npc.viewers.contains(uuid)) {
            return NpcDeletionResult.FAILED_NOT_SPAWNED
        }

        npc.despawn(uuid)

        return NpcDeletionResult.SUCCESS
    }

    override fun reShowNpc(
        npc: Npc,
        uuid: UUID
    ): NpcRespawnResult {
        if(!npcs.contains(npc)) {
            return NpcRespawnResult.FAILED_NOT_EXIST
        }

        if(npc.viewers.contains(uuid)) {
            return NpcRespawnResult.FAILED_ALREADY_SPAWNED
        }

        npc.despawn(uuid)
        npc.spawn(uuid)

        return NpcRespawnResult.SUCCESS
    }

    override fun setSkin(
        npc: Npc,
        skin: NpcSkin
    ) {
        npc.addProperty(BukkitNpcProperty(
            NpcProperty.Internal.SKIN_OWNER, skin.ownerName, propertyTypeRegistry.get(
                NpcPropertyType.Types.STRING) ?: error("STRING property type not found")
        ))
        npc.addProperty(BukkitNpcProperty(
            NpcProperty.Internal.SKIN_TEXTURE, skin.value, propertyTypeRegistry.get(
                NpcPropertyType.Types.STRING) ?: error("STRING property type not found")
        ))
        npc.addProperty(BukkitNpcProperty(
            NpcProperty.Internal.SKIN_SIGNATURE, skin.signature, propertyTypeRegistry.get(
                NpcPropertyType.Types.STRING) ?: error("STRING property type not found")
        ))
    }

    override fun setRotationType(
        npc: Npc,
        rotationType: NpcRotationType
    ) {
        npc.addProperty(BukkitNpcProperty(
            NpcProperty.Internal.ROTATION_TYPE, rotationType == NpcRotationType.PER_PLAYER, propertyTypeRegistry.get(
                NpcPropertyType.Types.BOOLEAN) ?: error("BOOLEAN property type not found")
        ))
    }

    override fun setRotation(
        npc: Npc,
        rotation: NpcRotation
    ) {
        npc.addProperty(BukkitNpcProperty(
            NpcProperty.Internal.ROTATION_FIXED, rotation, propertyTypeRegistry.get(
                NpcPropertyType.Types.NPC_ROTATION) ?: error("ROTATION property type not found")
        ))
    }

    override fun getNpc(id: Int): Npc? {
        return npcs.find { it.id == id }
    }

    override fun getNpc(uniqueName: String): Npc? {
        return npcs.find { it.uniqueName.equals(uniqueName, ignoreCase = true) }
    }

    override fun getNpcs(): ObjectList<Npc> {
        return npcs.toObjectList()
    }

    override fun despawnAllNpcs(): Int {
        val count = npcs.size

        npcs.forEach {
            val global = it.getPropertyValue(NpcProperty.Internal.VISIBILITY_GLOBAL, Boolean::class) ?: false
            if(global) {
                forEachPlayer { player ->
                    it.despawn(player.uniqueId)
                }
            } else {
                it.viewers.forEach { viewer ->
                    it.despawn(viewer)
                }
            }

            it.clearProperties()
            it.viewers.clear()
            it.delete()
        }

        npcs.clear()

        return count
    }

    override fun getProperties(npc: Npc): ObjectSet<NpcProperty> {
        return npc.properties.values.toObjectSet()
    }

    override fun addProperty(
        npc: Npc,
        property: NpcProperty
    ): Boolean {
        if (npc.hasProperty(property.key)) {
            npc.removeProperty(property.key)
        }

        npc.addProperty(property)
        return true
    }

    override fun removeProperty(
        npc: Npc,
        key: String
    ): Boolean {
        if (!npc.hasProperty(key)) {
            return false
        }

        npc.removeProperty(key)
        return true
    }
}