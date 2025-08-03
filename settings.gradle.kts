plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "surf-npc"
include("surf-npc-example")
include("surf-npc-api")
include("surf-npc-bukkit")
include("surf-npc-core")
include("surf-npc-example-dsl")