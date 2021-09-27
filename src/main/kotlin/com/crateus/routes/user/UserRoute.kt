package com.crateus.routes.user

import com.crateus.repos.UserRepository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import java.time.ZoneOffset
import java.util.*

fun Application.userRoutes() {
    routing {
        authenticate {
            userRoute()
        }
    }
}

fun Route.userRoute() {
    route("/user") {
        get("{id}") {
            val id = call.parameters["id"].run { UUID.fromString(this) }
            UserRepository().getUserById(id)?.run {
                UserDtos.UserDto(name, username, birthday.toInstant(ZoneOffset.UTC).toEpochMilli(), email)
            }.let {
                return@get if (it != null)
                    call.respond(HttpStatusCode.OK, it)
                else
                    call.respondText("No user found",status = HttpStatusCode.NotFound)
            }
        }
    }
}