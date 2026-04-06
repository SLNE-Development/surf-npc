package dev.slne.surf.npc.paper.util

import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose
import com.github.retrooper.packetevents.protocol.player.ClientVersion
import dev.slne.surf.api.core.service.PlayerLookupService
import dev.slne.surf.api.core.util.logger
import dev.slne.surf.api.core.util.toObjectSet
import dev.slne.surf.npc.api.npc.NpcPose
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.api.npc.skin.NpcSkinPart
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.bukkit.Location

suspend fun skinDataFromName(name: String): NpcSkin = withContext(Dispatchers.IO) {
    val uuid = PlayerLookupService.getUuid(name) ?: return@withContext NpcSkin.empty()

    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json()
        }
    }

    try {
        val response: HttpResponse =
            client.get("https://sessionserver.mojang.com/session/minecraft/profile/$uuid?unsigned=false") {
                timeout {
                    requestTimeoutMillis = 15_000
                }
            }

        if (!response.status.isSuccess()) {
            logger().atSevere()
                .log("Error retrieving skin data for $name: ${response.status.value} - ${response.status.description}")
            return@withContext NpcSkin.empty()
        }

        val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        val properties = json["properties"]?.jsonArray ?: return@withContext NpcSkin.empty()

        val textureProperty = properties.firstOrNull {
            it.jsonObject["name"]?.jsonPrimitive?.content == "textures"
        } ?: return@withContext NpcSkin.empty()

        val textureObj = textureProperty.jsonObject
        val value =
            textureObj["value"]?.jsonPrimitive?.content ?: return@withContext NpcSkin.empty()
        val signature =
            textureObj["signature"]?.jsonPrimitive?.content ?: return@withContext NpcSkin.empty()

        return@withContext NpcSkin(name, value, signature, NpcSkinPart.entries.toObjectSet())
    } catch (e: Exception) {
        logger().atSevere().log("Exception while retrieving skin data: ${e.message}")
        return@withContext NpcSkin.empty()
    } finally {
        client.close()
    }
}

fun Location.readableString(): String {
    return "${x.toInt()}, ${y.toInt()}, ${z.toInt()} in '${world.name}'"
}

fun NpcPose.toEntityPose(): EntityPose = EntityPose.getById(ClientVersion.getLatest(), this.id)
fun EntityPose.toNpcPose() = NpcPose[this.getId(ClientVersion.getLatest())]
