package com.crateus.features.auth

import com.crateus.features.auth.dtos.AuthDtos
import com.crateus.service.AuthenticationService
import com.crateus.service.UserService
import com.crateus.utils.ResultHandler
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject


fun Application.loginRoutes() {

    val userService: UserService by inject()
    val authenticationService: AuthenticationService by inject()

    routing {
        loginRoute(userService, authenticationService)
        registerUserRoute(userService)
    }
}

fun Route.loginRoute(userService: UserService, authenticationService: AuthenticationService) {

    route("/login") {
        post {
            call.run {
                receive<AuthDtos.LoginDto>().let { login ->
                    when (val result = userService.matchUserByEmail(login.username, login.password)) {
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



fun Route.registerUserRoute(userService: UserService) {
    route("/register") {
        post {
            call.run {
                val status = when (receive<AuthDtos.UserDto>().toModel().let { user -> userService.insertUser(user) }){
                    is ResultHandler.Success -> HttpStatusCode.OK
                    is ResultHandler.Failure -> HttpStatusCode.InternalServerError
                }
                call.respond(status)
            }
        }
    }
}