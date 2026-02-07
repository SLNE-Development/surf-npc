package dev.slne.surf.npc.bukkit.controller

import com.github.retrooper.packetevents.protocol.player.TextureProperty
import com.github.retrooper.packetevents.protocol.player.UserProfile
import com.github.retrooper.packetevents.util.Vector3d
import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.npc.api.event.NpcCreateEvent
import dev.slne.surf.npc.api.event.NpcHideEvent
import dev.slne.surf.npc.api.event.NpcShowEvent
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.NpcPose
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.bukkit.BukkitPackets
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.property.propertyTypeRegistry
import dev.slne.surf.npc.bukkit.util.sendPacket
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.random
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.util.*
import kotlin.math.atan2
import kotlin.math.sqrt

val npcController = NpcController()

class NpcController {
    private val _npcs = mutableObject2ObjectMapOf<String, Npc>()
    val npcs get() = _npcs.values.toObjectSet()

    fun createNpc(
        displayName: Component,
        uniqueName: String,
        type: EntityType,
        location: Location,
        viewers: ObjectSet<UUID>? = null,
        rotationType: NpcRotationType = NpcRotationType.PER_PLAYER,
        persistent: Boolean = false,
        skin: NpcSkin = NpcSkin.empty(),
    ): Npc {
        val npc = Npc(
            id = random.nextInt(),
            uniqueName = uniqueName,
            entityType = type,
            npcUuid = UUID.randomUUID(),
            nameTagId = random.nextInt(),
            nameTagUuid = UUID.randomUUID(),
            properties = mutableObject2ObjectMapOf<String, NpcProperty>(),
            viewers = viewers,
            npcSittingId = npcs.size + 1,
            npcSittingUuid = UUID.randomUUID()
        )

        npc.setRotationType(rotationType)
        npc.setPersistence(persistent)
        npc.setLocation(location)
        npc.setSkinData(skin)
        npc.setDisplayName(displayName)

        saveNpc(npc)
        npc.show()

        plugin.launch(plugin.globalRegionDispatcher) {
            NpcCreateEvent(npc).callEvent()
        }

        return npc
    }

    fun saveNpc(npc: Npc) {
        _npcs[npc.uniqueName] = npc
    }

    fun addViewer(npc: Npc, uuid: UUID) {
        val updated =
            npc.copy(viewers = (npc.viewers ?: mutableObjectSetOf()).also { it.add(uuid) })
        saveNpc(updated)

        showToViewer(npc, uuid)
    }

    fun removeViewer(npc: Npc, uuid: UUID) {
        val updated =
            npc.copy(viewers = (npc.viewers ?: mutableObjectSetOf()).also { it.remove(uuid) })
        saveNpc(updated)

        hideFromViewer(npc, uuid)
    }

    fun hasViewer(npc: Npc, uuid: UUID): Boolean = npc.viewers?.contains(uuid) ?: false
    fun clearViewers(npc: Npc) {
        npc.viewers?.forEach { hideFromViewer(npc, it) }
        val updated = npc.copy(viewers = mutableObjectSetOf())
        saveNpc(updated)
    }

    private fun showToViewer(npc: Npc, uuid: UUID) {
        val player = Bukkit.getPlayer(uuid) ?: return

        if (npc.entityType == EntityType.MANNEQUIN) {
            val userProfile = UserProfile(npc.npcUuid, npc.uniqueName)
            val skin = npc.getSkinData() ?: NpcSkin.empty()

            userProfile.textureProperties.add(
                TextureProperty(
                    "textures",
                    skin.value,
                    skin.signature
                )
            )

            BukkitPackets.NpcPackets.NpcSpawnPacket(
                npc.id,
                npc.npcUuid,
                npc.getLocation(),
                npc.entityType
            ).build().sendPacket(uuid)
            BukkitPackets.NpcPackets.NpcMetaDataPacket(npc)
                .build().sendPacket(uuid)

            BukkitPackets.NpcTeamPackets.TeamCreatePacket("npc_${npc.id}", npc.getDisplayName())
                .build()
                .sendPacket(uuid)
            BukkitPackets.NpcTeamPackets.TeamAddEntityPacket("npc_${npc.id}", npc.uniqueName)
                .build()
                .sendPacket(uuid)

            BukkitPackets.NpcNameTagPackets.NameTagSpawnPacket(
                npc.nameTagId,
                npc.nameTagUuid,
                npc.getLocation()
            ).build().sendPacket(uuid)
            BukkitPackets.NpcNameTagPackets.NameTagMetaDataPacket(
                npc.nameTagId,
                npc.getDisplayName()
            ).build()
                .sendPacket(uuid)

            refreshRotation(npc, uuid)

            plugin.launch(plugin.globalRegionDispatcher) {
                NpcShowEvent(player, npc).callEvent()
            }
        }
    }

    private fun hideFromViewer(npc: Npc, uuid: UUID) {
        val player = Bukkit.getPlayer(uuid) ?: return

        BukkitPackets.NpcPackets.NpcDestroyPacket(npc.id, npc.nameTagId).build().sendPacket(uuid)
        BukkitPackets.NpcPackets.NpcInfoRemovePacket(npc.npcUuid)

        plugin.launch(plugin.entityDispatcher(player)) {
            NpcHideEvent(
                player,
                npc
            ).callEvent()
        }
    }

    private fun refreshRotation(npc: Npc, uuid: UUID) {
        val rotationType = npc.getRotationType()
        val location = npc.getLocation()
        val player = Bukkit.getPlayer(uuid) ?: return

        val yawPitch: Pair<Float, Float> = when (rotationType) {
            NpcRotationType.FIXED -> {
                Pair(location.yaw, location.pitch)
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

        BukkitPackets.NpcPackets.NpcRotationPacket(npc.id, yawPitch.first, yawPitch.second)
            .build()
            .sendPacket(player.uniqueId)
        BukkitPackets.NpcPackets.NpcHeadRotationPacket(npc.id, yawPitch.first).build()
            .sendPacket(player.uniqueId)
    }

    fun editNpc(npc: Npc, edit: Npc.() -> Unit) {
        val updated = npc.apply(edit)

        saveNpc(updated)
        refreshNpc(updated)
    }

    fun teleport(npc: Npc, player: Player) {
        val location = player.location

        npc.addProperty(
            NpcProperty(
                NpcProperty.Internal.LOCATION,
                location,
                propertyTypeRegistry.get(
                    NpcPropertyType.Types.LOCATION_ID
                ) ?: error("LOCATION property type not found")
            )
        )
    }

    fun refreshNpc(npc: Npc) {
        npc.hide()
        npc.show()
    }

    fun refreshRotation(npc: Npc) {
        npc.forEachViewer {
            refreshRotation(npc, it)
        }
    }

    fun deleteNpc(npc: Npc) {
        npc.hide()

        _npcs.remove(npc.uniqueName)
    }

    fun showNpc(npc: Npc) {
        npc.forEachViewer {
            showToViewer(npc, it)
        }
    }

    fun hideNpc(npc: Npc) {
        npc.forEachViewer {
            hideFromViewer(npc, it)
        }
    }

    fun getNpc(id: Int): Npc? = npcs.find { it.id == id }
    fun getNpc(uniqueName: String): Npc? = npcs.find { it.uniqueName == uniqueName }
    fun getNpcs(): ObjectSet<Npc> = npcs

    fun setPose(npc: Npc, pose: NpcPose) {
        val location = npc.getPropertyValue(NpcProperty.Internal.LOCATION, Location::class)
            ?: error("Location is not set for NPC: ${npc.uniqueName}")

        npc.forEachViewer {
            if (pose == NpcPose.SITTING) {
                BukkitPackets.NpcExtraPackets.ExtraSpawnPacket(npc, location).build()
                    .sendPacket(it)
                BukkitPackets.NpcExtraPackets.ExtraMetaDataPacket(npc).build().sendPacket(it)
                BukkitPackets.NpcExtraPackets.ExtraMountPacket(npc).build().sendPacket(it)
            } else {
                BukkitPackets.NpcExtraPackets.ExtraDestroyPacket(npc).build().sendPacket(it)
                npc.refresh()
            }

            BukkitPackets.NpcPackets.NpcPoseChangePacket(npc.id, pose).build().sendPacket(it)
            BukkitPackets.NpcNameTagPackets.NameTagCorrectionPacket(
                npc.nameTagId,
                location,
                pose
            ).build().sendPacket(it)
        }
    }

}

fun locationOf(
    worldName: String,
    x: Double,
    y: Double,
    z: Double,
    yaw: Float = 0f,
    pitch: Float = 0f
) = Location(
    Bukkit.getWorld(worldName) ?: error("World '$worldName' not found"),
    x,
    y,
    z,
    yaw,
    pitch
)
