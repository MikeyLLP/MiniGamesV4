import dev.slne.surf.surfapi.gradle.util.withSurfApiBukkit

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

group = "de.mikeyllp"

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.50.1.0")
}

surfPaperPluginApi {
    mainClass("de.mikeyllp.miniGamesV4.MiniGamesV4")
    generateLibraryLoader(false)
    authors.add("MikeyLLP")

    runServer {
        withSurfApiBukkit()
    }
}