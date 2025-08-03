package dev.slne.surf.npc.bukkit.api

import com.google.auto.service.AutoService

import dev.slne.surf.npc.api.SurfNpcApi
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.result.NpcDeletionResult
import dev.slne.surf.npc.api.npc.rotation.NpcRotation
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.bukkit.npc.location.BukkitNpcLocation
import dev.slne.surf.npc.bukkit.property.BukkitNpcProperty
import dev.slne.surf.npc.bukkit.rotation.BukkitNpcRotation
import dev.slne.surf.npc.bukkit.skin.BukkitSNpcSkinData
import dev.slne.surf.npc.bukkit.util.skinDataFromName
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.npc.core.property.propertyTypeRegistry

import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet

import net.kyori.adventure.text.Component
import net.kyori.adventure.util.Services

import java.util.UUID

@AutoService(SurfNpcApi::class)
class BukkitSurfNpcApi : SurfNpcApi, Services.Fallback {
    override fun createNpc(
        displayName: Component,
        uniqueName: String,
        skin: NpcSkin,
        location: NpcLocation,
        global: Boolean,
        rotationType: NpcRotationType,
        fixedRotation: NpcRotation?
    ): NpcCreationResult {
        return npcController.createNpc(
            uniqueName,
            displayName,
            skin,
            location,
            rotationType,
            fixedRotation ?: BukkitNpcRotation(0f, 0f),
            global
        )
    }

    override fun deleteNpc(npc: Npc): NpcDeletionResult {
        return npcController.deleteNpc(npc)
    }

    override fun showNpc(npc: Npc, uuid: UUID) {
        npcController.showNpc(npc, uuid)
    }

    override fun hideNpc(npc: Npc, uuid: UUID) {
        npcController.hideNpc(npc, uuid)
    }

    override fun setSkin(npc: Npc, skin: NpcSkin) {
        npcController.setSkin(npc, skin)
    }

    override fun setRotationType(
        npc: Npc,
        rotationType: NpcRotationType
    ) {
        npcController.setRotationType(npc, rotationType)
    }

    override fun setRotation(npc: Npc, rotation: NpcRotation) {
        npcController.setRotation(npc, rotation)
    }

    override fun getProperties(npc: Npc): ObjectSet<NpcProperty> {
        return npcController.getProperties(npc)
    }

    override fun addProperty(
        npc: Npc,
        property: NpcProperty
    ): Boolean {
        return npcController.addProperty(npc, property)
    }

    override fun removeProperty(
        npc: Npc,
        key: String
    ): Boolean {
        return npcController.removeProperty(npc, key)
    }

    override fun createProperty(
        key: String,
        value: Any,
        type: NpcPropertyType
    ): NpcProperty {
        return BukkitNpcProperty(key, value, type)
    }

    override suspend fun getSkin(name: String): NpcSkin {
        return skinDataFromName(name)
    }

    override fun getNpc(id: Int): Npc? {
        return npcController.getNpc(id)
    }

    override fun getNpc(uniqueName: String): Npc? {
        return npcController.getNpc(uniqueName)
    }

    override fun getNpcs(): ObjectList<Npc> {
        return npcController.getNpcs()
    }

    override fun despawnAllNpcs() {
        npcController.despawnAllNpcs()
    }

    override fun createRotation(
        yaw: Float,
        pitch: Float
    ): NpcRotation {
        return BukkitNpcRotation(yaw, pitch)
    }

    override fun createSkinData(
        owner: String,
        value: String,
        signature: String
    ): NpcSkin {
        return BukkitSNpcSkinData(owner, value, signature)
    }

    override fun createLocation(
        x: Double,
        y: Double,
        z: Double,
        worldName: String
    ): NpcLocation {
        return BukkitNpcLocation(x, y, z, worldName)
    }

    override fun registerPropertyType(type: NpcPropertyType) {
        propertyTypeRegistry.register(type)
    }

    override fun unregisterPropertyType(type: NpcPropertyType) {
        propertyTypeRegistry.unregister(type)
    }

    override fun getPropertyType(id: String): NpcPropertyType? {
        return propertyTypeRegistry.get(id)
    }
}