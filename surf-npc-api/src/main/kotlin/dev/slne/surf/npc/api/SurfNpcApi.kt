package dev.slne.surf.npc.api

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.NpcPose
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.util.*

interface SurfNpcApi {
    fun createNpc(
        displayName: Component,
        uniqueName: String,
        type: EntityType,
        location: Location,
        viewers: ObjectSet<UUID>? = null,
        rotationType: NpcRotationType = NpcRotationType.PER_PLAYER,
        persistent: Boolean = false,
        skin: NpcSkin = NpcSkin.empty(),
    ): Npc

    fun fetchSkin(username: String): NpcSkin

    fun saveNpc(npc: Npc)
    fun addViewer(npc: Npc, uuid: UUID)
    fun removeViewer(npc: Npc, uuid: UUID)
    fun hasViewer(npc: Npc, uuid: UUID): Boolean
    fun clearViewers(npc: Npc)

    fun teleport(npc: Npc, player: Player)

    fun editNpc(npc: Npc, edit: Npc.() -> Unit)
    fun refreshNpc(npc: Npc)
    fun refreshRotation(npc: Npc)
    fun deleteNpc(npc: Npc)
    fun showNpc(npc: Npc)
    fun hideNpc(npc: Npc)

    fun setDisplayName(npc: Npc, displayName: Component)
    fun setSkinData(npc: Npc, skin: NpcSkin)
    fun setLocation(npc: Npc, location: Location)
    fun setPersistence(npc: Npc, persistent: Boolean)
    fun setRotationType(npc: Npc, rotationType: NpcRotationType)

    fun getDisplayName(npc: Npc): Component
    fun getSkinData(npc: Npc): NpcSkin?
    fun getLocation(npc: Npc): Location
    fun isPersistent(npc: Npc): Boolean
    fun getRotationType(npc: Npc): NpcRotationType

    fun getProperties(npc: Npc): ObjectSet<NpcProperty>
    fun addProperty(npc: Npc, property: NpcProperty)
    fun getPropertyTypeOrThrow(id: String): NpcPropertyType

    fun getNpc(id: Int): Npc?
    fun getNpc(uniqueName: String): Npc?
    fun getNpcs(): ObjectSet<Npc>

    fun setPose(npc: Npc, pose: NpcPose)

    companion object {
        val INSTANCE = requiredService<SurfNpcApi>()
    }
}

val surfNpcApi get() = SurfNpcApi.INSTANCE