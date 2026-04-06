import dev.slne.surf.api.gradle.util.registerRequired

plugins {
    id("dev.slne.surf.api.gradle.paper-plugin")
}

dependencies {
    compileOnly(project(":surf-npc-api"))
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.npc.example.SurfNpcExamplePlugin")
    authors.add("red")
    generateLibraryLoader(false)
    foliaSupported(true)
    serverDependencies {
        registerRequired("surf-npc-paper")
    }
}