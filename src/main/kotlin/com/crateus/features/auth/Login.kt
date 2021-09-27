package com.crateus.features.auth

import com.crateus.features.auth.dtos.AuthDtos
import com.crateus.features.auth.dtos.Mapper
import com.crateus.service.AuthenticationService
import com.crateus.service.UserService
import com.crateus.service.HashManager
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
            val login = call.receive<AuthDtos.LoginDto>()

            // Check username and password
            UserService().getUserByEmail(login.username).let { user ->
                if (user == null || HashManager.passwordsDontMatch(password = login.password, hash = user.hash)) {
                    return@post call.respondText(
                        "No user found, username or password incorrect!",
                        status = HttpStatusCode.NotFound
                    )
                }
            }
            val token = authenticationService.createJwt(application = application, claim = login.username)
            call.run {
                response.header(HttpHeaders.Authorization, "Bearer $token")
                respond(HttpStatusCode.OK)
            }
        }
    }
}



fun Route.registerUserRoute() {
    route("/register") {
        post {
            val newUser = call.receive<AuthDtos.UserDto>()
            val user = Mapper.dtoToUser(newUser)
            val id = UserService().insertUser(user)
            val status = if (id._value != null) HttpStatusCode.OK else HttpStatusCode.InternalServerError
            call.respond(status)
        }
    }
}