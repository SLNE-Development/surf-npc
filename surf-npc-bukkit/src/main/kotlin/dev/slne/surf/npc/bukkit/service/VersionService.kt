package dev.slne.surf.npc.bukkit.service

import dev.slne.surf.npc.bukkit.util.SurfPluginVersion

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI

class VersionService {
    var currentVersion = SurfPluginVersion.local()
    var latestVersion: SurfPluginVersion? = null
    var link: String? = null

    private val fetchUrl = "https://api.github.com/repos/SLNE-DEVELOPMENT/surf-npc/releases/latest"

    fun isUpToDate() = !(latestVersion?.isNewerThen(currentVersion) ?: false)

    suspend fun fetchGithubVersion() = withContext(Dispatchers.IO) {
        runCatching {
            val url = URI(fetchUrl).toURL()
            val connection = url.openConnection() as HttpURLConnection

            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json")

            if (connection.responseCode == 200) {
                val body =
                    BufferedReader(InputStreamReader(connection.inputStream)).use { it.readText() }
                val json = Json.parseToJsonElement(body).jsonObject
                val latestTag = json["tag_name"]?.jsonPrimitive?.content
                val link = json["url"]?.jsonPrimitive?.content

                link?.let {
                    this@VersionService.link = it
                }

                latestTag?.let {
                    this@VersionService.latestVersion =
                        SurfPluginVersion.fromString(latestTag.removePrefix("v"))
                }
            }

            connection.disconnect()
        }
    }

    companion object {
        val INSTANCE = VersionService()
    }
}

val versionService get() = VersionService.INSTANCE