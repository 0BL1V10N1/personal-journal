plugins {
    id("com.android.application") version "8.12.3" apply false
    id("org.jetbrains.kotlin.android") version "2.3.10" apply false
    id("org.jlleitschuh.gradle.ktlint") version "14.0.1" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.8" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.3.10" apply false
}

subprojects {
    plugins.withId("io.gitlab.arturbosch.detekt") {
        tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
            jvmTarget = "17"
            config.setFrom(rootDir.resolve("config/detekt/detekt.yml"))
            buildUponDefaultConfig = true
        }
    }
}
