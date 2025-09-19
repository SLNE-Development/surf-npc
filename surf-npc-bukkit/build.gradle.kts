plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-npc-core"))
}

version = "1.21.7-1.0.0-SNAPSHOT"

surfPaperPluginApi {
    mainClass("dev.slne.surf.npc.bukkit.BukkitMain")
    authors.add("red")
    foliaSupported(true)

    generateLibraryLoader(false)
}