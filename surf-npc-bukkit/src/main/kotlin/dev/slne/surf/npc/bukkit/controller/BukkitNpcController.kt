package dev.slne.surf.npc.bukkit.controller

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.google.auto.service.AutoService
import dev.slne.surf.npc.api.event.NpcCreateEvent
import dev.slne.surf.npc.api.event.NpcDeleteEvent
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.NpcCreatorType
import dev.slne.surf.npc.api.npc.NpcPose
import dev.slne.surf.npc.api.npc.animation.NpcAnimationType
import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotation
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.api.result.*
import dev.slne.surf.npc.bukkit.npc.BukkitNpc
import dev.slne.surf.npc.bukkit.npc.property.BukkitNpcProperty
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.core.controller.NpcController
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.surfapi.core.api.util.*
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.util.Services
import java.util.*

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
        viewers: ObjectSet<UUID>?,
        persistent: Boolean,
        glowing: Boolean,
        glowingColor: NamedTextColor,
        npcCreatorType: NpcCreatorType
    ): NpcCreationResult {
        val id = random.nextInt()
        val nameTagId = random.nextInt()
        val uuid = UUID.randomUUID()
        val nameTagUuid = UUID.randomUUID()

        if (this.getNpc(id) != null) {
            return NpcCreationResult.Failure(NpcCreationFailureReason.ALREADY_EXISTS)
        }

        if (this.getNpc(uniqueName) != null) {
            return NpcCreationResult.Failure(NpcCreationFailureReason.ALREADY_EXISTS)
        }

        val npc = BukkitNpc(
            id,
            mutableObject2ObjectMapOf<String, NpcProperty>(),
            viewers,
            uuid,
            nameTagId,
            nameTagUuid,
            uniqueName
        )

        val componentType = propertyTypeRegistry.get(
            NpcPropertyType.Types.COMPONENT
        ) ?: error("Component property type not found")

        val booleanType = propertyTypeRegistry.get(
            NpcPropertyType.Types.BOOLEAN
        ) ?: error("BOOLEAN property type not found")

        val npcLocationType = propertyTypeRegistry.get(
            NpcPropertyType.Types.NPC_LOCATION
        ) ?: error("NPC_LOCATION property type not found")
        val npcRotationPropertyType = propertyTypeRegistry.get(
            NpcPropertyType.Types.NPC_ROTATION
        ) ?: error("NPC_ROTATION property type not found")
        val namedTextColorType = propertyTypeRegistry.get(
            NpcPropertyType.Types.NAMED_TEXT_COLOR
        ) ?: error("NAMED_TEXT_COLOR property type not found")
        val skinDataType = propertyTypeRegistry.get(
            NpcPropertyType.Types.SKIN_DATA
        ) ?: error("SKIN_DATA property type not found")
        val npcCreatorPropertyType = propertyTypeRegistry.get(
            NpcPropertyType.Types.NPC_CREATOR_TYPE
        ) ?: error("NPC_CREATOR_TYPE property type not found")

        npc.addProperties(
            Triple(
                NpcProperty.Internal.DISPLAYNAME, displayName, componentType
            ),
            Triple(
                NpcProperty.Internal.SKIN_DATA, skinData, skinDataType
            ),
            Triple(
                NpcProperty.Internal.LOCATION, location, npcLocationType
            ),
            Triple(
                NpcProperty.Internal.ROTATION_TYPE,
                rotationType == NpcRotationType.PER_PLAYER,
                booleanType
            ),
            Triple(
                NpcProperty.Internal.ROTATION_FIXED, rotation, npcRotationPropertyType
            ),
            Triple(
                NpcProperty.Internal.PERSISTENCE, persistent, booleanType
            ),
            Triple(
                NpcProperty.Internal.GLOWING_ENABLED, glowing, booleanType
            ),
            Triple(
                NpcProperty.Internal.GLOWING_COLOR, glowingColor, namedTextColorType
            ),
            Triple(
                NpcProperty.Internal.CREATOR_TYPE, npcCreatorType, npcCreatorPropertyType
            )
        )

        this.registerNpc(npc)

        npc.forEachViewer {
            npc.spawn(it)
        }

        plugin.launch(plugin.globalRegionDispatcher) {
            NpcCreateEvent(npc).callEvent()
        }

        return NpcCreationResult.Success(npc)
    }

    override fun deleteNpc(npc: Npc): NpcDeletionResult {
        if (!this.unregisterNpc(npc)) {
            return NpcDeletionResult.FAILED_NOT_FOUND
        }

        npc.forEachViewer {
            npc.despawn(it)
        }

        npc.clearProperties()

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
        if (!npcs.contains(npc)) {
            return NpcSpawnResult.Failure(NpcSpawnFailureReason.NOT_EXIST)
        }

        npc.viewers?.let {
            if (it.contains(uuid)) {
                return NpcSpawnResult.Failure(NpcSpawnFailureReason.ALREADY_SPAWNED)
            }
        }

        npc.spawn(uuid)

        return NpcSpawnResult.Success(npc)
    }

    override fun hideNpc(
        npc: Npc,
        uuid: UUID
    ): NpcDeletionResult {
        if (!npcs.contains(npc)) {
            return NpcDeletionResult.FAILED_NOT_FOUND
        }

        npc.viewers?.let {
            if (!it.contains(uuid)) {
                return NpcDeletionResult.FAILED_NOT_SPAWNED
            }
        }

        npc.despawn(uuid)

        return NpcDeletionResult.SUCCESS
    }

    override fun reShowNpc(
        npc: Npc,
        uuid: UUID
    ): NpcRespawnResult {
        if (!npcs.contains(npc)) {
            return NpcRespawnResult.Failure(NpcRespawnFailureReason.NOT_EXIST)
        }

        npc.despawn(uuid)
        npc.spawn(uuid)

        return NpcRespawnResult.Success(npc)
    }

    override fun setSkin(
        npc: Npc,
        skin: NpcSkin
    ) {
        npc.addProperty(
            BukkitNpcProperty(
                NpcProperty.Internal.SKIN_DATA, skin, propertyTypeRegistry.get(
                    NpcPropertyType.Types.SKIN_DATA
                ) ?: error("SKIN_DATA property type not found")
            )
        )
    }

    override fun setRotationType(
        npc: Npc,
        rotationType: NpcRotationType
    ) {
        npc.addProperty(
            BukkitNpcProperty(
                NpcProperty.Internal.ROTATION_TYPE,
                rotationType == NpcRotationType.PER_PLAYER,
                propertyTypeRegistry.get(
                    NpcPropertyType.Types.BOOLEAN
                ) ?: error("BOOLEAN property type not found")
            )
        )
    }

    override fun setRotation(
        npc: Npc,
        rotation: NpcRotation
    ) {
        npc.addProperty(
            BukkitNpcProperty(
                NpcProperty.Internal.ROTATION_FIXED, rotation, propertyTypeRegistry.get(
                    NpcPropertyType.Types.NPC_ROTATION
                ) ?: error("ROTATION property type not found")
            )
        )
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
            it.forEachViewer { viewer ->
                it.despawn(viewer)
            }

            it.clearProperties()
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

    override fun playAnimation(
        npc: Npc,
        animationType: NpcAnimationType
    ) {
        npc.playAnimation(animationType)
    }

    override fun setPose(npc: Npc, pose: NpcPose) {
        npc.setPose(pose)
    }
}