package com.crateus.routes.helloworld

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.helloTest() {
    routing {
        authenticate {
            getHelloWorld()
        }
    }
}

fun Route.getHelloWorld() {
    route("hello"){
        get {
            call.respond(HttpStatusCode.OK)
        }
    }
}