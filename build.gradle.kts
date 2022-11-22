val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.21"
    id("io.ktor.plugin") version "2.1.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.21"
    id("org.openapi.generator") version "6.2.1"
}

group = "name.valery1707.problem.rshb"
version = "0.0.1"
application {
    mainClass.set("name.valery1707.problem.rshb.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-compression-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-default-headers-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlin_version")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

openApiGenerate {
    generatorName.set("kotlin")
    //Downloaded from https://api.svoe-rodnoe.ru/api/static/index.html, reformatted, removed values from enum in query params
    inputSpec.set("$rootDir/src/main/open-api/rshb/svoe-rodnoe.json")
    outputDir.set("$buildDir/generated/open-api/rshb/svoe-rodnoe")
    val apiPkg = "ru.rshb.svoe.rodnoe"
    apiPackage.set("$apiPkg.api")
    invokerPackage.set("$apiPkg.invoker")
    modelPackage.set("$apiPkg.model")
    configOptions.put("library", "multiplatform")
    configOptions.put("dateLibrary", "java8")
    configOptions.put("enumPropertyNaming", "PascalCase")
    configOptions.put("packageName", "$apiPkg.client")
}
sourceSets {
    main {
        java {
            srcDir("$buildDir/generated/open-api/rshb/svoe-rodnoe/src/main/kotlin")
        }
    }
}
//todo Declarative dependency
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn("openApiGenerate")
}
