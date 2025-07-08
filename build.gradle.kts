import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    java
    id("de.eldoria.plugin-yml.paper") version "0.7.1"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "9.0.0-beta17"
}

group = "de.mikeyllp"
version = "0.3.1-beta" // Version of the plugin

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        url = uri("https://repo.codemc.org/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.codemc.io/repository/maven-releases/")
    }
    maven {
        url = uri("https://repo.codemc.io/repository/maven-snapshots/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.7-R0.1-SNAPSHOT")
    compileOnly("dev.jorel:commandapi-bukkit-core:10.1.1")
    implementation("com.github.stefvanschie.inventoryframework:IF:0.11.1")
    implementation("org.xerial:sqlite-jdbc:3.50.1.0")
}

paper {
    main = "de.mikeyllp.miniGamesV4.MiniGamesV4"
    apiVersion = "1.21"
    name = "MiniGamesV4"
    author = "MikeyLLP"

    serverDependencies {
        register("CommandAPI") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
}

tasks {
    runServer {
        minecraftVersion("1.21.7")

        downloadPlugins {
            hangar("commandapi", "10.1.1")
        }
    }

    shadowJar {
        relocate("com.github.stefvanschie.inventoryframework", "de.mikeyllp.miniGamesV4.libs.if")
    }
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
