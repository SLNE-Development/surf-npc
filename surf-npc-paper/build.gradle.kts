plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-npc-api"))
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.npc.paper.PaperMain")
    authors.add("red")
    foliaSupported(true)

    generateLibraryLoader(false)
}