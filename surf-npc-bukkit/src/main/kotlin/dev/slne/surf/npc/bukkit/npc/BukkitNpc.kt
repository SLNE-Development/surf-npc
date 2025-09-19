package dev.slne.surf.npc.bukkit.npc

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.player.TextureProperty
import com.github.retrooper.packetevents.protocol.player.UserProfile
import com.github.retrooper.packetevents.util.Vector3d
import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.npc.api.event.NpcEvent
import dev.slne.surf.npc.api.event.NpcHideEvent
import dev.slne.surf.npc.api.event.NpcShowEvent
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.NpcEventHandler
import dev.slne.surf.npc.api.npc.NpcPose
import dev.slne.surf.npc.api.npc.animation.NpcAnimationType
import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotation
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.bukkit.*
import dev.slne.surf.npc.bukkit.npc.location.BukkitNpcLocation
import dev.slne.surf.npc.bukkit.npc.property.BukkitNpcProperty
import dev.slne.surf.npc.bukkit.npc.rotation.BukkitNpcRotation
import dev.slne.surf.npc.bukkit.util.toLocation
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.surfapi.bukkit.api.glow.glowingApi
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.mutableObjectListOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.math.atan2
import kotlin.math.sqrt
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class BukkitNpc(
    override val id: Int,
    override val npcUuid: UUID,
    override val nameTagId: Int,
    override val nameTagUuid: UUID,
    override val properties: Object2ObjectMap<String, NpcProperty>,
    override val viewers: ObjectSet<UUID>?,
    override val uniqueName: String,
    override var npcSittingId: Int,
    override var npcSittingUuid: UUID
) : Npc {
    private val eventHandlers =
        mutableObject2ObjectMapOf<KClass<out NpcEvent>, ObjectList<NpcEventHandler<*>>>()

    override fun spawn(uuid: UUID) {
        val packetEvents = PacketEvents.getAPI()
        val playerManager = packetEvents.playerManager

        val player = Bukkit.getPlayer(uuid) ?: return
        val user = playerManager.getUser(player)

        val displayName =
            this.getPropertyValue(NpcProperty.Internal.DISPLAYNAME, Component::class) ?: return
        val profile = UserProfile(npcUuid, uniqueName)

        val skinData =
            this.getPropertyValue(NpcProperty.Internal.SKIN_DATA, NpcSkin::class) ?: return

        val rotation =
            this.getPropertyValue(NpcProperty.Internal.ROTATION_FIXED, NpcRotation::class) ?: return
        val location =
            this.getPropertyValue(NpcProperty.Internal.LOCATION, NpcLocation::class) ?: return

        val glowing =
            this.getPropertyValue(NpcProperty.Internal.GLOWING_ENABLED, Boolean::class) ?: false
        val glowingColor =
            this.getPropertyValue(NpcProperty.Internal.GLOWING_COLOR, NamedTextColor::class)
                ?: NamedTextColor.WHITE

        profile.textureProperties.add(
            TextureProperty(
                "textures",
                skinData.value,
                skinData.signature
            )
        )

        val rotationPair = Pair(
            rotation.yaw,
            rotation.pitch
        )

        user.sendPacket(createPlayerInfoPacket(profile, displayName))
        user.sendPacket(
            createPlayerSpawnPacket(
                id,
                npcUuid,
                location.toLocation(),
                rotationPair.first,
                rotationPair.second
            )
        )
        user.sendPacket(createEntityMetadataPacket(id, skinData.skinByte()))

        user.sendPacket(createTeamCreatePacket("npc_$id", displayName))
        user.sendPacket(createTeamAddEntityPacket("npc_$id", uniqueName))

        user.sendPacket(createNametagSpawnPacket(nameTagId, nameTagUuid, location.toLocation()))
        user.sendPacket(createNametagMetadataPacket(nameTagId, displayName))

        if (glowing) {
            glowingApi.makeGlowing(id, "npc_$id", player, glowingColor)
        }

        plugin.launch(plugin.entityDispatcher(player)) {
            NpcShowEvent(player, this@BukkitNpc).callEvent()
        }
    }

    override fun despawn(uuid: UUID) {
        val packetEvents = PacketEvents.getAPI()
        val playerManager = packetEvents.playerManager

        val player = Bukkit.getPlayer(uuid) ?: return
        val user = playerManager.getUser(player)

        user.sendPacket(createDestroyPacket(this.id, nameTagId))
        user.sendPacket(createPlayerInfoRemovePacket(npcUuid))

        plugin.launch(plugin.entityDispatcher(player)) {
            NpcHideEvent(
                player,
                this@BukkitNpc
            ).callEvent()
        }
    }

    override fun refresh() {
        forEachViewer {
            despawn(it)
            spawn(it)

            playAnimation(NpcAnimationType.SWING_ARM_MAIN)
        }
    }

    override fun refreshRotation(uuid: UUID) {
        val player = Bukkit.getPlayer(uuid) ?: return
        val user = PacketEvents.getAPI().playerManager.getUser(player)

        val rotationType =
            if (this.getPropertyValue(NpcProperty.Internal.ROTATION_TYPE, Boolean::class)
                    ?: error("Rotation type is not set for NPC: $uniqueName")
            ) NpcRotationType.PER_PLAYER else NpcRotationType.FIXED
        val fixedRotation =
            this.getPropertyValue(NpcProperty.Internal.ROTATION_FIXED, NpcRotation::class)
                ?: BukkitNpcRotation(0f, 0f)
        val location = this.getPropertyValue(NpcProperty.Internal.LOCATION, NpcLocation::class)
            ?: error("Location is not set for NPC: $uniqueName")

        val yawPitch: Pair<Float, Float> = when (rotationType) {
            NpcRotationType.FIXED -> {
                Pair(fixedRotation.yaw, fixedRotation.pitch)
            }

            NpcRotationType.PER_PLAYER -> {
                val npcVec = Vector3d(location.x, location.y, location.z)
                val playerLoc = player.location

                val dx = playerLoc.x - npcVec.x
                val dz = playerLoc.z - npcVec.z
                val dy = playerLoc.y - npcVec.y

                val yaw = Math.toDegrees(atan2(-dx, dz)).toFloat()
                val horizontalDist = sqrt((dx * dx) + (dz * dz))
                val pitch = (-Math.toDegrees(atan2(dy, horizontalDist))).toFloat()

                Pair(yaw, pitch)
            }
        }

        val rotationPackets = createRotationPackets(id, yawPitch.first, yawPitch.second)

        user.sendPacket(rotationPackets.first)
        user.sendPacket(rotationPackets.second)
    }


    override fun delete() {
        npcController.deleteNpc(this)
    }

    override fun teleport(player: Player) {
        val location = player.location

        this.addProperty(
            BukkitNpcProperty(
                NpcProperty.Internal.LOCATION,
                BukkitNpcLocation(location.x, location.y, location.z, location.world.name),
                propertyTypeRegistry.get(
                    NpcPropertyType.Types.NPC_LOCATION
                ) ?: error("LOCATION property type not found")
            )
        )

        forEachViewer {
            val player = Bukkit.getPlayer(it) ?: return@forEachViewer
            val user = PacketEvents.getAPI().playerManager.getUser(player)

            user.sendPacket(createTeleportPacket(id, location))
            user.sendPacket(
                createTeleportPacket(
                    nameTagId,
                    location.clone().add(0.0, 2.0, 0.0)
                )
            )
        }
    }

    override fun retrieveViewers(): ObjectSet<UUID> =
        viewers ?: Bukkit.getOnlinePlayers().map { it.uniqueId }.toObjectSet()

    override fun forEachViewer(action: (UUID) -> Unit) = retrieveViewers().forEach { action(it) }

    override fun show() {
        forEachViewer {
            this.spawn(it)
        }
    }

    override fun hide() {
        forEachViewer {
            this.despawn(it)
        }
    }

    override fun addProperty(property: NpcProperty) {
        properties[property.key] = property
    }

    override fun addProperties(vararg properties: Triple<String, Any, NpcPropertyType>) = properties
        .map { BukkitNpcProperty(it.first, it.second, it.third) }
        .forEach { this.addProperty(it) }

    override fun addProperties(vararg properties: NpcProperty) {
        properties.forEach {
            addProperty(it)
        }
    }

    override fun getProperty(key: String): NpcProperty? {
        return properties.values.find { it.key == key }
    }

    override fun removeProperty(key: String) {
        properties.remove(key)
    }

    override fun hasProperty(key: String): Boolean {
        return properties.any { it.key == key }
    }

    override fun clearProperties() {
        properties.clear()
    }

    override fun hasProperties(): Boolean {
        return properties.isNotEmpty()
    }

    override fun <T : NpcEvent> addEventHandler(
        eventClass: KClass<T>,
        handler: NpcEventHandler<T>
    ) {
        eventHandlers.computeIfAbsent(eventClass) { mutableObjectListOf() }
            .add(handler)
    }

    override fun <T : NpcEvent> removeEventHandler(
        eventClass: KClass<T>,
        handler: NpcEventHandler<T>
    ) {
        eventHandlers[eventClass]?.remove(handler)
    }

    override fun <T : NpcEvent> callHandlers(event: T) {
        val handlers = eventHandlers[event::class] ?: return
        for (handler in handlers) {
            (handler as NpcEventHandler<T>)(event)
        }
    }

    override fun playAnimation(animationType: NpcAnimationType) {
        val packetEvents = PacketEvents.getAPI()
        val playerManager = packetEvents.playerManager

        forEachViewer {
            val player = Bukkit.getPlayer(it) ?: return@forEachViewer
            val user = playerManager.getUser(player)

            user.sendPacket(createEntityAnimation(id, animationType))
        }
    }

    override fun setPose(pose: NpcPose) {
        val packetEvents = PacketEvents.getAPI()
        val playerManager = packetEvents.playerManager

        val location = this.getPropertyValue(NpcProperty.Internal.LOCATION, NpcLocation::class)
            ?: error("Location is not set for NPC: $uniqueName")

        forEachViewer {
            val player = Bukkit.getPlayer(it) ?: return@forEachViewer
            val user = playerManager.getUser(player)

            if (pose == NpcPose.SITTING) {
                user.sendPacket(createSpawnSittingArmorStandPacket(this, location.toLocation()))
                user.sendPacket(createSittingArmorStandMetadataPacket(this))
                user.sendPacket(createMountSittingArmorStandPacket(this))
            } else {
                user.sendPacket(createDestroySittingArmorStandPacket(this))
                refresh()
            }

            user.sendPacket(createPoseChangePacket(id, pose))
            user.sendPacket(createCorrectNameTagPacket(nameTagId, location.toLocation(), pose))
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (other !is Npc) {
            return false
        }

        if (this.npcUuid == other.npcUuid) {
            return true
        }

        return false
    }

    override fun <T : Any> getPropertyValue(key: String, clazz: KClass<T>): T? {
        val propertyValue = this.getProperty(key)?.value ?: return null

        if (!clazz.isInstance(propertyValue)) {
            return null
        }

        return propertyValue as T
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + nameTagId
        result = 31 * result + npcSittingId
        result = 31 * result + npcUuid.hashCode()
        result = 31 * result + nameTagUuid.hashCode()
        result = 31 * result + properties.hashCode()
        result = 31 * result + (viewers?.hashCode() ?: 0)
        result = 31 * result + uniqueName.hashCode()
        result = 31 * result + npcSittingUuid.hashCode()
        result = 31 * result + eventHandlers.hashCode()
        return result
    }
}