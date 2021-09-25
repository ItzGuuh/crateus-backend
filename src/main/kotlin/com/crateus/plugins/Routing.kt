package com.crateus.plugins

import com.crateus.routes.auth.loginRoutes
import com.crateus.routes.helloworld.helloTest
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.auth.*

fun Application.configureRouting(securityVariables: SecurityVariables) {
    loginRoutes(securityVariables)
    helloTest()
}