package com.crateus.plugins

import com.crateus.routes.auth.loginRoutes
import com.crateus.routes.helloworld.helloTest
import com.crateus.routes.user.userRoutes
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*

fun Application.configureRouting(securityVariables: SecurityVariables) {
    nonAuthenticatedRoutes(securityVariables)
    authenticatedRoutes()
}

fun Application.nonAuthenticatedRoutes(securityVariables: SecurityVariables) {
    loginRoutes(securityVariables)
}

fun Application.authenticatedRoutes() {
    routing {
        helloTest()
        userRoutes()
    }
}