plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

repositories {
    maven {
        name = "Canvas"
        url = uri("https://maven.canvasmc.io/snapshots")
    }
}

dependencies {
    api(project(":surf-npc-api"))
    compileOnly("io.canvasmc.canvas:canvas-api:1.21.11-R0.1-SNAPSHOT")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.npc.paper.PaperMain")
    authors.add("red")
    foliaSupported(true)

    generateLibraryLoader(false)
}

configurations.all {
    resolutionStrategy.capabilitiesResolution.withCapability("org.bukkit:bukkit") {
        select("io.canvasmc.canvas:canvas-api:1.21.11-R0.1-SNAPSHOT")
    }
}