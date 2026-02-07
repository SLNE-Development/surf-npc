package dev.slne.surf.npc.bukkit.util

import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose
import com.github.retrooper.packetevents.protocol.player.ClientVersion
import dev.slne.surf.npc.api.npc.NpcPose
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.api.npc.skin.NpcSkinPart
import dev.slne.surf.surfapi.core.api.service.PlayerLookupService
import dev.slne.surf.surfapi.core.api.util.logger
import dev.slne.surf.surfapi.core.api.util.toObjectSet
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
    val uuid = PlayerLookupService.getUuid(name) ?: return@withContext skinDataDefault()

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
            return@withContext skinDataDefault()
        }

        val json = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        val properties = json["properties"]?.jsonArray ?: return@withContext skinDataDefault()

        val textureProperty = properties.firstOrNull {
            it.jsonObject["name"]?.jsonPrimitive?.content == "textures"
        } ?: return@withContext skinDataDefault()

        val textureObj = textureProperty.jsonObject
        val value =
            textureObj["value"]?.jsonPrimitive?.content ?: return@withContext skinDataDefault()
        val signature =
            textureObj["signature"]?.jsonPrimitive?.content ?: return@withContext skinDataDefault()

        return@withContext NpcSkin(name, value, signature, NpcSkinPart.entries.toObjectSet())
    } catch (e: Exception) {
        logger().atSevere().log("Exception while retrieving skin data: ${e.message}")
        return@withContext skinDataDefault()
    } finally {
        client.close()
    }
}


fun skinDataDefault(): NpcSkin {
    return NpcSkin(
        "default",
        "ewogICJ0aW1lc3RhbXAiIDogMTY3NDQxNzAzMzQ2MCwKICAicHJvZmlsZUlkIiA6ICIzY2MxMTY3MWU0MTM0ODM0YjhjMmZjMTY1OGE4OWU3OCIsCiAgInByb2ZpbGVOYW1lIiA6ICJSb3NzaTU2IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2QwZGEwZjE0YTg1YjQzODNjYWFiNGM2NWJlOTBkMzMyNWEzMGIwY2UwNGI2YzA4ODQzODcxMDIwNDkwZDk0N2IiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
        "VjpajnLZ4dIdfrXQJFFyV7Fp/IKEcQmEZIbiVR3MtFalVQ8BS5wCv8dsQeTmwZbv4zo4lL1urA4boAC9NIcP8KK+ucg19kgGrNDTs3Gmo7j57kZipDqKHJZMaHcvGx2Vur61YNL+cLQ9kwHaPwWdEdd9SDCJuXpvlBLrIxM8bqacyE1S/aATU388373xg2rNPCOxUy4YhScqeDSkBYdlYJ6xqQTwVIZZ8iwE/rqjq/X66VMRL6EtwwOqGqUBusvIiAfnGsyqMJ4rtohRsF9YHOR4cIk8K6E12ryl8uyp/HAkOYvfuk7TQuQSZpfgkH0aObjAeFPLGGGbV+P1cyezZm0I3erOQhviN9zfeaiuTHBmfP+RSb6dw+eQVKkdE2GkNinRp5EX5pltR3wREos2Muh2LXw3gGgWR3HvKnAZ1ofgAcIGk6pq9IKTpGaRZYhjIMpMXGsiRlgYqN4BmEDhTlV1XIldpatju6QnbTYyr8oJ97Z5FEVkhQ+5GsQnU9Rx+GyANhJjGVU06WiS/3H42SxbKaoPPIbBGPN3H8JTWO5Av3Y2zYLhWLhPFKF0b09FbhmkMJq0W9qE82DoByY/mhRmwTb1yrbKwXwxScEC4CSvbQBxwNjtp2WwttYs3L/x4Sxf4PgkPL0lvUF+yt+OGkHgJw4EzxV2giJaLfsXmsQ=",
        NpcSkinPart.entries.toObjectSet()
    )
}

fun Location.readableString(): String {
    return "${x.toInt()}, ${y.toInt()}, ${z.toInt()} in '${world.name}'"
}

fun NpcPose.toEntityPose(): EntityPose = EntityPose.getById(ClientVersion.getLatest(), this.id)
fun EntityPose.toNpcPose() = NpcPose[this.getId(ClientVersion.getLatest())]
