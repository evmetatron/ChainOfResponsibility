plugins {
    id("maven-publish")
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    id("java-test-fixtures")
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "com.github.evmetatron"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

configurations.all {
    exclude(module = "slf4j-log4j12")
}

dependencies {
    implementation("org.springframework:spring-context:5.3.23")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.4.1")
    testImplementation("io.mockk:mockk:1.12.5")
}

detekt {
    source = files(
        "$rootDir/src",
    )
    config = files("$rootDir/detekt/detekt.yaml")
    buildUponDefaultConfig = true
    autoCorrect = true

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("spring-kotlin-chain-of-responsibility") {
            from(components["java"])
        }
    }
}