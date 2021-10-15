package com.crateus.features.user

import com.crateus.features.user.dtos.UserDtos
import com.crateus.service.UserService
import com.crateus.utils.ResultHandler
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
            return@get when (val resultHandler = UserService().getUserById(id)) {
                is ResultHandler.Failure -> call.respondText("User not found",status = HttpStatusCode.NotFound)
                is ResultHandler.Success -> resultHandler.value.run {
                    UserDtos.UserDto(name, username, birthday.toInstant(ZoneOffset.UTC).toEpochMilli(), email)
                }.let { call.respond(HttpStatusCode.OK, it) }
            }
        }
    }
}