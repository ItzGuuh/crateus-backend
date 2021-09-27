package com.crateus.plugins

import com.crateus.features.auth.loginRoutes
import com.crateus.features.helloworld.helloTest
import com.crateus.features.user.userRoutes
import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureRouting() {
    nonAuthenticatedRoutes()
    authenticatedRoutes()
}

fun Application.nonAuthenticatedRoutes() {
    loginRoutes()
}

fun Application.authenticatedRoutes() {
    routing {
        helloTest()
        userRoutes()
    }
}