package dev.slne.surf.npc.api.npc

import dev.slne.surf.api.core.util.mutableObject2ObjectMapOf
import dev.slne.surf.api.core.util.mutableObjectListOf
import dev.slne.surf.api.core.util.toObjectSet
import dev.slne.surf.npc.api.SurfNpcApi
import dev.slne.surf.npc.api.event.NpcEvent
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
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
import org.bukkit.util.BoundingBox
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
    lateinit var boundingBox: BoundingBox
    lateinit var rotationBox: BoundingBox

    fun updateBoundingBoxes() {
        val location = getLocation()
        boundingBox = BoundingBox(
            location.x - 0.5,
            location.y,
            location.z - 0.5,
            location.x + 0.5,
            location.y + 1.8,
            location.z + 0.5
        )

        rotationBox = BoundingBox(
            location.x - 20,
            location.y - 20,
            location.z - 20,
            location.x + 20,
            location.y + 20,
            location.z + 20
        )
    }

    private val eventHandlers =
        mutableObject2ObjectMapOf<KClass<out NpcEvent>, ObjectList<NpcEventHandler<*>>>()

    fun show() = SurfNpcApi.showNpc(this)
    fun hide() = SurfNpcApi.hideNpc(this)

    fun refresh() = SurfNpcApi.refreshNpc(this)
    fun refreshRotation() = SurfNpcApi.refreshRotation(this)

    fun delete() = SurfNpcApi.deleteNpc(this)
    fun teleport(player: Player) = SurfNpcApi.teleport(this, player)
    fun retrieveViewers(): ObjectSet<UUID> =
        viewers ?: Bukkit.getOnlinePlayers().map { it.uniqueId }.toObjectSet()

    fun forEachViewer(action: (UUID) -> Unit) = retrieveViewers().forEach(action)

    fun addProperty(property: NpcProperty) = SurfNpcApi.editNpc(this) {
        this.properties[property.key] = property
    }

    fun addProperties(vararg properties: Triple<String, Any, NpcPropertyType>) =
        SurfNpcApi.editNpc(this) {
            properties.forEach { (key, value, type) ->
                this.properties[key] = NpcProperty(key, value, type)
            }
        }

    fun addProperties(vararg properties: NpcProperty) = SurfNpcApi.editNpc(this) {
        properties.forEach { this.properties[it.key] = it }
    }

    fun getProperty(key: String): NpcProperty? = properties[key]

    fun setEquipment(slot: EquipmentSlot, item: ItemStack) =
        SurfNpcApi.setEquipment(this, slot, item)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getPropertyValue(key: String, clazz: KClass<T>): T? =
        getProperty(key)?.value as? T

    fun removeProperty(key: String) = SurfNpcApi.editNpc(this) {
        this.properties.remove(key)
    }

    fun hasProperty(key: String): Boolean = getProperty(key) != null
    fun clearProperties() = SurfNpcApi.editNpc(this) {
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

    fun save() = SurfNpcApi.saveNpc(this)

    fun addViewer(uuid: UUID) = SurfNpcApi.addViewer(this, uuid)
    fun removeViewer(uuid: UUID) = SurfNpcApi.removeViewer(this, uuid)
    fun hasViewer(uuid: UUID): Boolean = SurfNpcApi.hasViewer(this, uuid)
    fun clearViewers() = SurfNpcApi.clearViewers(this)

    fun setDisplayName(displayName: Component) = SurfNpcApi.setDisplayName(this, displayName)
    fun setSkinData(skin: NpcSkin) = SurfNpcApi.setSkinData(this, skin)
    fun setLocation(location: Location) = SurfNpcApi.setLocation(this, location)
    fun setPersistence(persistent: Boolean) = SurfNpcApi.setPersistence(this, persistent)
    fun setRotationType(rotationType: NpcRotationType) =
        SurfNpcApi.setRotationType(this, rotationType)

    fun setScale(scale: Double) = SurfNpcApi.setScale(this, scale)

    fun getDisplayName(): Component = SurfNpcApi.getDisplayName(this)
    fun getSkinData(): NpcSkin? = SurfNpcApi.getSkinData(this)
    fun getLocation(): Location = SurfNpcApi.getLocation(this)
    fun getPose(): NpcPose = SurfNpcApi.getPose(this)
    fun isPersistent(): Boolean = SurfNpcApi.isPersistent(this)
    fun getRotationType(): NpcRotationType = SurfNpcApi.getRotationType(this)
    fun getScale(): Double = SurfNpcApi.getScale(this)

    fun setPose(pose: NpcPose) = SurfNpcApi.setPose(this, pose)
}