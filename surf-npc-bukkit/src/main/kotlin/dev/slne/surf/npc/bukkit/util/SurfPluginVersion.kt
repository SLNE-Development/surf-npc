package dev.slne.surf.npc.bukkit.util

import dev.slne.surf.npc.bukkit.plugin

data class SurfPluginVersion(
    val mcVersion: String,
    val pluginVersion: String,
    val isSnapshot: Boolean = false
) {
    override fun toString(): String {
        return "$mcVersion-$pluginVersion${if (isSnapshot) "-SNAPSHOT" else ""}"
    }

    fun isNewerThan(other: SurfPluginVersion?): Boolean {
        if (other == null) return true

        val mcPartsThis = this.mcVersion.split(".").mapNotNull { it.toIntOrNull() }
        val mcPartsOther = other.mcVersion.split(".").mapNotNull { it.toIntOrNull() }
        val mcLength = maxOf(mcPartsThis.size, mcPartsOther.size)

        for (i in 0 until mcLength) {
            val thisPart = mcPartsThis.getOrNull(i) ?: 0
            val otherPart = mcPartsOther.getOrNull(i) ?: 0
            if (thisPart > otherPart) return true
            if (thisPart < otherPart) return false
        }

        val pluginPartsThis = this.pluginVersion.split(".").mapNotNull { it.toIntOrNull() }
        val pluginPartsOther = other.pluginVersion.split(".").mapNotNull { it.toIntOrNull() }
        val pluginLength = maxOf(pluginPartsThis.size, pluginPartsOther.size)

        for (i in 0 until pluginLength) {
            val thisPart = pluginPartsThis.getOrNull(i) ?: 0
            val otherPart = pluginPartsOther.getOrNull(i) ?: 0
            if (thisPart > otherPart) return true
            if (thisPart < otherPart) return false
        }

        return this.isSnapshot && !other.isSnapshot
    }


    companion object {
        fun local(): SurfPluginVersion {
            val desc = plugin.pluginMeta
            val version = desc.version

            val parts = version.split("-")
            val mcVersion = parts.getOrNull(0) ?: "unknown"
            val pluginVersion = parts.getOrNull(1) ?: "unknown"
            val isSnapshot = parts.size > 2 && parts[2].equals("SNAPSHOT", ignoreCase = true)

            return SurfPluginVersion(
                mcVersion = mcVersion,
                pluginVersion = pluginVersion,
                isSnapshot = isSnapshot
            )
        }

        fun fromString(version: String): SurfPluginVersion {
            val parts = version.split("-")
            val mcVersion = parts.getOrNull(0) ?: "unknown"
            val pluginVersion = parts.getOrNull(1) ?: "unknown"
            val isSnapshot = parts.size > 2 && parts[2].equals("SNAPSHOT", ignoreCase = true)

            return SurfPluginVersion(mcVersion, pluginVersion, isSnapshot)
        }
    }

}
