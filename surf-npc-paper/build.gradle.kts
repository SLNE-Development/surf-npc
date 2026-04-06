plugins {
    id("dev.slne.surf.api.gradle.paper-plugin")
}

repositories {
    maven {
        name = "Canvas"
        url = uri("https://maven.canvasmc.io/snapshots")
    }
}

dependencies {
    api(project(":surf-npc-api"))
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.npc.paper.PaperMain")
    authors.add("red")
    foliaSupported(true)

    useCanvasMc()
    generateLibraryLoader(false)
}