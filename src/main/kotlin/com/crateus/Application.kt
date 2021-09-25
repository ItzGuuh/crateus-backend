package com.crateus

import io.ktor.application.*
import com.crateus.plugins.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.serialization.*

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    installFeatures()
    configure()
}

private fun Application.configure() {
    val securityVariables = configureSecurity()
    configureRouting(securityVariables)
}

private fun Application.installFeatures() {
    install(ContentNegotiation) {
        json()
    }
}