package com.crateus.routes.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.crateus.plugins.SecurityVariables
import com.crateus.repos.UserRepository
import com.crateus.service.HashManager
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*


fun Application.loginRoutes(securityVariables: SecurityVariables) {
    routing {
        loginRoute(securityVariables)
        registerUserRoute()
    }
}

fun Route.loginRoute(securityVariables: SecurityVariables) {
    route("/login") {
        post {
            val login = call.receive<AuthDtos.LoginDto>()

            // Check username and password
            UserRepository().getUserByEmail(login.username).let { user ->
                if (user == null || HashManager.passwordsDontMatch(password = login.password, hash = user.hash)) {
                    return@post call.respondText(
                        "No user found, username or password incorrect!",
                        status = HttpStatusCode.NotFound
                    )
                }
            }
            val token = createJwt(securityVariables, login)
            call.run {
                response.header(HttpHeaders.Authorization, "Bearer $token")
                respond(HttpStatusCode.OK)
            }
        }
    }
}

private fun createJwt(
    securityVariables: SecurityVariables,
    login: AuthDtos.LoginDto
) = JWT.create()
    .withAudience(securityVariables.audience)
    .withIssuer(securityVariables.issuer)
    .withClaim("username", login.username)
    .withExpiresAt(Date(System.currentTimeMillis() + 60000))
    .sign(Algorithm.HMAC256(securityVariables.secret))

fun Route.registerUserRoute() {
    route("/register") {
        post {
            val newUser = call.receive<AuthDtos.UserDto>()
            val user = Mapper.dtoToUser(newUser)
            val id = UserRepository().insertUser(user)
            val status = if (id._value != null) HttpStatusCode.OK else HttpStatusCode.InternalServerError
            call.respond(status)
        }
    }
}