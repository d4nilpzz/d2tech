plugins {
    id("java-library")
    id("idea")
    id("net.neoforged.moddev") version "2.0.140"
}

val minecraftVersion: String by extra
val modVersion: String by extra
val neoVersion: String by extra
val parchmentMappingsVersion: String by extra
val parchmentMinecraftVersion: String by extra
val neoVersionRange: String by extra
val loaderVersionRange: String by extra
val jeiVersion: String by extra

version = modVersion
group = "dev.d4nilpzz"

repositories {
    mavenLocal()
    maven {
        name = "JEI"
        url = uri("https://maven.blamejared.com")
    }
}

base {
    archivesName = "d2tech"
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    version = neoVersion

    parchment {
        mappingsVersion = parchmentMappingsVersion
        minecraftVersion = parchmentMinecraftVersion
    }

    // accessTransformers.add("src/main/resources/META-INF/accesstransformer.cfg")

    runs {
        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", "d2tech")
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", "d2tech")
        }

        create("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", "d2tech")
        }

        create("data") {
            data()
            programArguments.addAll("--mod", "d2tech", "--all", "--output", file("src/generated/resources/").getAbsolutePath(), "--existing", file("src/main/resources/").getAbsolutePath())
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        create("d2tech") {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main.get().resources { srcDir("src/generated/resources") }


dependencies {
    // JEI (soft dependency)
    compileOnly("mezz.jei:jei-1.21.1-neoforge:${jeiVersion}")
    runtimeOnly("mezz.jei:jei-1.21.1-neoforge:${jeiVersion}")
}

tasks.processResources {
    var replaceProperties = mapOf("modVersion" to modVersion, "loaderVersionRange" to loaderVersionRange,
        "neoVersionRange" to neoVersionRange, "minecraftVersion" to minecraftVersion)

    inputs.properties(replaceProperties)
    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
