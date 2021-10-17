package com.crateus.features.userInfo

import com.crateus.features.userInfo.dtos.UserDtos
import com.crateus.models.User
import com.crateus.service.UserService
import com.crateus.utils.ResultHandler
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import java.time.ZoneOffset
import java.util.*

fun Application.userRoutes() {
    val userService: UserService by inject()
    routing {
        authenticate {
            userRoute(userService)
            usersRoute(userService)
        }
    }
}

fun Route.usersRoute(userService: UserService){
    route("/users") {
        get("/") {
            call.run {
                when (val result = userService.getAllUsers()) {
                    is ResultHandler.Failure -> respondText(text = result.message, status = HttpStatusCode.InternalServerError)
                    is ResultHandler.Success -> {
                        val user = result.value.map { UserDtos.UserDto(it.name, it.username, it.birthday.toInstant(ZoneOffset.UTC).toEpochMilli(), it.email) }
                        respond(status = HttpStatusCode.OK, user)
                    }
                }
            }
        }
    }
}

fun Route.userRoute(userService: UserService) {
    route("/user") {
        get("{id}") {
            val id = call.parameters["id"].run { UUID.fromString(this) }
            return@get when (val resultHandler = userService.getUserById(id)) {
                is ResultHandler.Failure -> call.respondText(resultHandler.message, status = HttpStatusCode.NotFound)
                is ResultHandler.Success -> resultHandler.value.run {
                    UserDtos.UserDto(name, username, birthday.toInstant(ZoneOffset.UTC).toEpochMilli(), email)
                }.let { call.respond(HttpStatusCode.OK, it) }
            }
        }
    }
}