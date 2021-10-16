package com.crateus.plugins

import com.crateus.features.auth.loginRoutes
import com.crateus.features.makeANote.noteRoutes
import com.crateus.features.userInfo.userRoutes
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
    userRoutes()
    noteRoutes()
}