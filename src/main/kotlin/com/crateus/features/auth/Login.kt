package com.crateus.features.auth

import com.crateus.features.auth.dtos.AuthDtos
import com.crateus.features.auth.dtos.Mapper
import com.crateus.service.AuthenticationService
import com.crateus.service.UserService
import com.crateus.service.HashManager
import com.crateus.utils.ResultHandler
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject


fun Application.loginRoutes() {
    routing {
        loginRoute()
        registerUserRoute()
    }
}

fun Route.loginRoute() {
    val authenticationService: AuthenticationService by inject()
    route("/login") {
        post {
            call.run {
                receive<AuthDtos.LoginDto>().let { login ->
                    when (val result = UserService().matchUserByEmail(login.username, login.password)) {
                        is ResultHandler.Failure -> respondText(
                            text = result.message,
                            status = HttpStatusCode.NotFound
                        )
                        is ResultHandler.Success -> {
                            authenticationService.createJwt(application = application, usernameClaim = login.username)
                                .let { token ->
                                    response.header(HttpHeaders.Authorization, "Bearer $token")
                                    respond(HttpStatusCode.OK)
                                }
                        }
                    }
                }
            }
        }
    }
}



fun Route.registerUserRoute() {
    route("/register") {
        post {
            call.run {
                val id =
                    receive<AuthDtos.UserDto>().let { newUser -> Mapper.dtoToUser(newUser) }.let { user -> UserService().insertUser(user) }
                val status = if (id._value != null) HttpStatusCode.OK else HttpStatusCode.InternalServerError
                call.respond(status)
            }
        }
    }
}