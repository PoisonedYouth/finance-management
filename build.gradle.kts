@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.versionUpdate)
    alias(libs.plugins.catalogUpdate)
    alias(libs.plugins.pitest)
    jacoco
}

group = "com.poisonedyouth"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // ktor
    implementation(libs.ktorServerCore)
    implementation(libs.ktorServerAuth)
    implementation(libs.ktorServerContentNegotiation)
    implementation(libs.ktorServerJackson)
    implementation(libs.ktorServerNetty)

    // exposed
    implementation(libs.exposedCore)
    implementation(libs.exposedJdbc)

    // koin
    implementation(libs.koinKtor)
    implementation(libs.koinLogger)

    // logging
    implementation(libs.logback)

    // arrow
    implementation(platform(libs.arrowStack))
    implementation(libs.arrowCore)

    // persistence
    implementation(libs.postgresqlDriver)
    implementation(libs.hikari)
    implementation(libs.h2Driver)

    // testing
    testImplementation(libs.junit5Engine)
    testImplementation(libs.junit5Api)
    testImplementation(libs.kotestRunnerJunit5)
    testImplementation(libs.kotestCoreAssertion)
    testImplementation(libs.kotestArrowAssert)
    testImplementation(libs.ktorServerTestHost)

    testImplementation(libs.testcontainersPostresql)
    testImplementation(libs.testContainersJunit5)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

val jdkVersion: String = libs.versions.jdkVersion.get()
kotlin {
    jvmToolchain {
        this.languageVersion.set(JavaLanguageVersion.of(jdkVersion))
    }
}

kotlin {
    explicitApi()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
}

pitest {
    setProperty("junit5PluginVersion", libs.versions.pitestJunit5Version)
    setProperty("testPlugin", "junit5")
    setProperty("targetClasses", listOf("com.poisonedyouth.financemanagement.*"))
    setProperty("mutationThreshold", 10)
    setProperty("outputFormats", listOf("HTML"))
    setProperty("threads", 8)
    setProperty("withHistory", true)
    setProperty("failWhenNoMutations", false)
    setProperty("useClasspathFile", true)
    setProperty("jvmArgs", listOf("-Xmx16G"))
    setProperty("avoidCallsTo", listOf("kotlin.jvm.internal"))
}
