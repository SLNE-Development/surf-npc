package dev.slne.surf.npc.api.npc

import dev.slne.surf.npc.api.event.NpcEvent
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.mutableObjectListOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.reflect.KClass


/**
 * A type alias representing an event handler for NPC-related events.
 *
 * @param T The type of the event that will be handled.
 */
typealias NpcEventHandler<T> = (T) -> Unit

/**
 * Represents a non-player character (NPC) in the game.
 */
data class Npc(
    val id: Int,
    val uniqueName: String,
    val entityType: EntityType,
    val npcUuid: UUID,
    val nameTagId: Int,
    val nameTagUuid: UUID,
    val properties: Object2ObjectMap<String, NpcProperty>,
    val viewers: ObjectSet<UUID>?,
    val npcSittingId: Int,
    val npcSittingUuid: UUID
) {
    private val eventHandlers =
        mutableObject2ObjectMapOf<KClass<out NpcEvent>, ObjectList<NpcEventHandler<*>>>()

    fun show() = surfNpcApi.showNpc(this)
    fun hide() = surfNpcApi.hideNpc(this)

    fun refresh() = surfNpcApi.refreshNpc(this)
    fun refreshRotation(uuid: UUID) = surfNpcApi.refreshRotation(this)

    fun delete() = surfNpcApi.deleteNpc(this)
    fun teleport(player: Player) = surfNpcApi.teleport(this, player)
    fun retrieveViewers(): ObjectSet<UUID> =
        viewers ?: Bukkit.getOnlinePlayers().map { it.uniqueId }.toObjectSet()

    fun forEachViewer(action: (UUID) -> Unit) = retrieveViewers().forEach(action)

    fun addProperty(property: NpcProperty) = surfNpcApi.editNpc(this) {
        this.properties[property.key] = property
    }

    fun addProperties(vararg properties: Triple<String, Any, NpcPropertyType>) =
        surfNpcApi.editNpc(this) {
            properties.forEach { (key, value, type) ->
                this.properties[key] = NpcProperty(key, value, type)
            }
        }

    fun addProperties(vararg properties: NpcProperty) = surfNpcApi.editNpc(this) {
        properties.forEach { this.properties[it.key] = it }
    }

    fun getProperty(key: String): NpcProperty? = properties[key]

    fun setEquipment(slot: EquipmentSlot, item: ItemStack) =
        surfNpcApi.setEquipment(this, slot, item)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getPropertyValue(key: String, clazz: KClass<T>): T? =
        getProperty(key)?.value as? T

    fun removeProperty(key: String) = surfNpcApi.editNpc(this) {
        this.properties.remove(key)
    }

    fun hasProperty(key: String): Boolean = getProperty(key) != null
    fun clearProperties() = surfNpcApi.editNpc(this) {
        this.properties.clear()
    }

    fun hasProperties(): Boolean = properties.isNotEmpty()

    fun isStatic() =
        properties.any { it.key == NpcProperty.Internal.PERSISTENCE && it.value.value as? Boolean ?: false }

    fun <T : NpcEvent> addEventHandler(eventClass: KClass<T>, handler: NpcEventHandler<T>) =
        eventHandlers.computeIfAbsent(eventClass) { mutableObjectListOf() }
            .add(handler)

    fun <T : NpcEvent> removeEventHandler(eventClass: KClass<T>, handler: NpcEventHandler<T>) =
        eventHandlers[eventClass]?.remove(handler)

    @Suppress("UNCHECKED_CAST")
    fun <T : NpcEvent> callHandlers(event: T) =
        eventHandlers[event::class]?.forEach { (it as NpcEventHandler<T>)(event) }

    fun save() = surfNpcApi.saveNpc(this)

    fun addViewer(uuid: UUID) = surfNpcApi.addViewer(this, uuid)
    fun removeViewer(uuid: UUID) = surfNpcApi.removeViewer(this, uuid)
    fun hasViewer(uuid: UUID): Boolean = surfNpcApi.hasViewer(this, uuid)
    fun clearViewers() = surfNpcApi.clearViewers(this)

    fun setDisplayName(displayName: Component) = surfNpcApi.setDisplayName(this, displayName)
    fun setSkinData(skin: NpcSkin) = surfNpcApi.setSkinData(this, skin)
    fun setLocation(location: Location) = surfNpcApi.setLocation(this, location)
    fun setPersistence(persistent: Boolean) = surfNpcApi.setPersistence(this, persistent)
    fun setRotationType(rotationType: NpcRotationType) =
        surfNpcApi.setRotationType(this, rotationType)

    fun getDisplayName(): Component = surfNpcApi.getDisplayName(this)
    fun getSkinData(): NpcSkin? = surfNpcApi.getSkinData(this)
    fun getLocation(): Location = surfNpcApi.getLocation(this)
    fun isPersistent(): Boolean = surfNpcApi.isPersistent(this)
    fun getRotationType(): NpcRotationType = surfNpcApi.getRotationType(this)

    fun setPose(pose: NpcPose) = surfNpcApi.setPose(this, pose)
}