package dev.slne.surf.npc.paper.api

import com.google.auto.service.AutoService
import dev.slne.surf.api.core.util.toObjectSet
import dev.slne.surf.npc.api.SurfNpcApi
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.NpcPose
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.paper.controller.npcController
import dev.slne.surf.npc.paper.property.propertyTypeRegistry
import dev.slne.surf.npc.paper.util.skinDataFromName
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import net.kyori.adventure.util.Services
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

@AutoService(SurfNpcApi::class)
class PaperSurfNpcApi : SurfNpcApi, Services.Fallback {
    override fun createNpc(
        displayName: Component,
        uniqueName: String,
        type: EntityType,
        location: Location,
        viewers: ObjectSet<UUID>?,
        rotationType: NpcRotationType,
        persistent: Boolean,
        skin: NpcSkin
    ) = npcController.createNpc(
        displayName,
        uniqueName,
        type,
        location,
        viewers,
        rotationType,
        persistent,
        skin
    )

    override suspend fun fetchSkin(username: String): NpcSkin = skinDataFromName(username)

    override fun saveNpc(npc: Npc) = npcController.saveNpc(npc)

    override fun addViewer(npc: Npc, uuid: UUID) = npcController.addViewer(npc, uuid)
    override fun removeViewer(npc: Npc, uuid: UUID) = npcController.removeViewer(npc, uuid)
    override fun hasViewer(
        npc: Npc,
        uuid: UUID
    ) = npcController.hasViewer(npc, uuid)

    override fun clearViewers(npc: Npc) = npcController.clearViewers(npc)

    override fun teleport(npc: Npc, player: Player) = npcController.teleport(npc, player)
    override fun editNpc(
        npc: Npc,
        edit: Npc.() -> Unit
    ) = npcController.editNpc(npc, edit)

    override fun setEquipment(npc: Npc, slot: EquipmentSlot, item: ItemStack) =
        npcController.setEquipment(npc, slot, item)

    override fun refreshNpc(npc: Npc) = npcController.refreshNpc(npc)
    override fun refreshRotation(npc: Npc) = npcController.refreshRotation(npc)
    override fun deleteNpc(npc: Npc) = npcController.deleteNpc(npc)
    override fun showNpc(npc: Npc) = npcController.showNpc(npc)
    override fun hideNpc(npc: Npc) = npcController.hideNpc(npc)
    override fun setDisplayName(
        npc: Npc,
        displayName: Component
    ) = npcController.editNpc(npc) {
        this.addProperty(
            NpcProperty(
                NpcProperty.Internal.DISPLAYNAME,
                displayName,
                NpcPropertyType.Types.COMPONENT_TYPE
            )
        )
    }

    override fun setSkinData(
        npc: Npc,
        skin: NpcSkin
    ) = npcController.editNpc(npc) {
        this.addProperty(
            NpcProperty(
                NpcProperty.Internal.SKIN_DATA,
                skin,
                NpcPropertyType.Types.SKIN_DATA_TYPE
            )
        )
    }

    override fun setLocation(npc: Npc, location: Location) = npcController.editNpc(npc) {
        this.addProperty(
            NpcProperty(
                NpcProperty.Internal.LOCATION,
                location,
                NpcPropertyType.Types.LOCATION_TYPE
            )
        )
    }

    override fun setPersistence(npc: Npc, persistent: Boolean) = npcController.editNpc(npc) {
        this.addProperty(
            NpcProperty(
                NpcProperty.Internal.PERSISTENCE,
                persistent,
                NpcPropertyType.Types.BOOLEAN_TYPE
            )
        )
    }

    override fun setRotationType(
        npc: Npc,
        rotationType: NpcRotationType
    ) = npcController.editNpc(npc) {
        this.addProperty(
            NpcProperty(
                NpcProperty.Internal.ROTATION_TYPE,
                rotationType,
                NpcPropertyType.Types.ROTATION_TYPE_TYPE
            )
        )
    }

    override fun getDisplayName(npc: Npc) =
        npc.getPropertyValue(NpcProperty.Internal.DISPLAYNAME, Component::class)
            ?: Component.empty()

    override fun getSkinData(npc: Npc) =
        npc.getPropertyValue(NpcProperty.Internal.SKIN_DATA, NpcSkin::class)

    override fun getLocation(npc: Npc) =
        npc.getPropertyValue(NpcProperty.Internal.LOCATION, Location::class) ?: Location(
            null,
            0.0,
            0.0,
            0.0
        )

    override fun isPersistent(npc: Npc) =
        npc.getPropertyValue(NpcProperty.Internal.PERSISTENCE, Boolean::class) ?: false

    override fun getRotationType(npc: Npc) =
        npc.getPropertyValue(NpcProperty.Internal.ROTATION_TYPE, NpcRotationType::class)
            ?: NpcRotationType.PER_PLAYER

    override fun getProperties(npc: Npc) = npc.properties.values.toObjectSet()

    override fun addProperty(
        npc: Npc,
        property: NpcProperty
    ) = npc.addProperty(property)

    override fun getPropertyTypeOrThrow(id: String) =
        propertyTypeRegistry.get(id) ?: error("Property type with id '$id' not found")

    override fun getNpc(id: Int) = npcController.getNpc(id)
    override fun getNpc(uniqueName: String) = npcController.getNpc(uniqueName)
    override fun getNpcs() = npcController.npcs
    override fun setPose(
        npc: Npc,
        pose: NpcPose
    ) = npcController.setPose(npc, pose)
}