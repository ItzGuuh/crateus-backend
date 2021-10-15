import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposedVersion: String by project
val koin_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.30"
    kotlin("plugin.serialization") version "1.5.30"
    id("org.flywaydb.flyway") version "5.2.4"
}

group = "com.crateus"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.21")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Authenticator (JWT)
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")

    // Kotlin serialization
    implementation("io.ktor:ktor-serialization:$ktor_version")

    // Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")

    // PostgreSQL
    implementation("org.postgresql:postgresql:42.2.24.jre7")

    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("org.flywaydb:flyway-core:8.0.0")


    // Koin core features
    implementation("io.insert-koin:koin-core:$koin_version")
    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:$koin_version")

    // password4j (hashing password)
    implementation ("com.password4j:password4j:1.5.3")

    // Tests
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    // Koin test features
    testImplementation("io.insert-koin:koin-test:$koin_version")
}

flyway {
    /*url = System.getenv("DB_URL")
    user = System.getenv("DB_USER")
    password = System.getenv("DB_PASSWORD")*/
//    url = System.getenv("postgresql://postgres:Wo9BgkRXnJf8TLWDbz2nJ79b@localhost:5432/crateus")
    url = System.getenv("jdbc:postgresql://localhost:5432/crateus?user=postgres&password=Wo9BgkRXnJf8TLWDbz2nJ79b")
    user = System.getenv("postgresql")
    password = System.getenv("Wo9BgkRXnJf8TLWDbz2nJ79b")
    baselineOnMigrate=true
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

configurations.all{
    exclude("org.slf4j", "slf4j-nop")
}