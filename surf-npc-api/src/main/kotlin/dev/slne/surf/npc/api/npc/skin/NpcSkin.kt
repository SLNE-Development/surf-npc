package dev.slne.surf.npc.api.npc.skin

import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet

data class NpcSkin(
    val ownerName: String,
    val value: String,
    val signature: String,
    val parts: ObjectSet<NpcSkinPart>
) {
    fun skinByte(): Byte {
        var skinByte = 0
        if (NpcSkinPart.CAPE in parts) skinByte = skinByte or 0x01
        if (NpcSkinPart.JACKET in parts) skinByte = skinByte or 0x02
        if (NpcSkinPart.LEFT_SLEEVES in parts) skinByte = skinByte or 0x04
        if (NpcSkinPart.RIGHT_SLEEVES in parts) skinByte = skinByte or 0x08
        if (NpcSkinPart.LEFT_PANTS_LEG in parts) skinByte = skinByte or 0x10
        if (NpcSkinPart.RIGHT_PANTS_LEG in parts) skinByte = skinByte or 0x20
        if (NpcSkinPart.HAT in parts) skinByte = skinByte or 0x40
        return skinByte.toByte()
    }

    companion object {
        fun empty() = NpcSkin(
            "default",
            "ewogICJ0aW1lc3RhbXAiIDogMTY3NDQxNzAzMzQ2MCwKICAicHJvZmlsZUlkIiA6ICIzY2MxMTY3MWU0MTM0ODM0YjhjMmZjMTY1OGE4OWU3OCIsCiAgInByb2ZpbGVOYW1lIiA6ICJSb3NzaTU2IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2QwZGEwZjE0YTg1YjQzODNjYWFiNGM2NWJlOTBkMzMyNWEzMGIwY2UwNGI2YzA4ODQzODcxMDIwNDkwZDk0N2IiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
            "VjpajnLZ4dIdfrXQJFFyV7Fp/IKEcQmEZIbiVR3MtFalVQ8BS5wCv8dsQeTmwZbv4zo4lL1urA4boAC9NIcP8KK+ucg19kgGrNDTs3Gmo7j57kZipDqKHJZMaHcvGx2Vur61YNL+cLQ9kwHaPwWdEdd9SDCJuXpvlBLrIxM8bqacyE1S/aATU388373xg2rNPCOxUy4YhScqeDSkBYdlYJ6xqQTwVIZZ8iwE/rqjq/X66VMRL6EtwwOqGqUBusvIiAfnGsyqMJ4rtohRsF9YHOR4cIk8K6E12ryl8uyp/HAkOYvfuk7TQuQSZpfgkH0aObjAeFPLGGGbV+P1cyezZm0I3erOQhviN9zfeaiuTHBmfP+RSb6dw+eQVKkdE2GkNinRp5EX5pltR3wREos2Muh2LXw3gGgWR3HvKnAZ1ofgAcIGk6pq9IKTpGaRZYhjIMpMXGsiRlgYqN4BmEDhTlV1XIldpatju6QnbTYyr8oJ97Z5FEVkhQ+5GsQnU9Rx+GyANhJjGVU06WiS/3H42SxbKaoPPIbBGPN3H8JTWO5Av3Y2zYLhWLhPFKF0b09FbhmkMJq0W9qE82DoByY/mhRmwTb1yrbKwXwxScEC4CSvbQBxwNjtp2WwttYs3L/x4Sxf4PgkPL0lvUF+yt+OGkHgJw4EzxV2giJaLfsXmsQ=",
            NpcSkinPart.entries.toObjectSet()
        )
    }
}