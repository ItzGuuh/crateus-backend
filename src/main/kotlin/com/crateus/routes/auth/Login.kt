package com.crateus.routes.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.crateus.models.users
import com.crateus.plugins.SecurityVariables
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class LoginDto(val username: String, val password: String)

fun Application.loginRoutes(securityVariables: SecurityVariables) {
    routing {
        loginRoute(securityVariables)
    }
}

fun Route.loginRoute(securityVariables: SecurityVariables) {
    route("/login") {
        post {
            val login = call.receive<LoginDto>()

            // Check username and password
            users.find { it.username == login.username && it.hash == login.password }?: return@post call.respondText(
                "No user found",
                status = HttpStatusCode.NotFound
            )

            val token = JWT.create()
                .withAudience(securityVariables.audience)
                .withIssuer(securityVariables.issuer)
                .withClaim("username", login.username)
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(securityVariables.secret))
            call.run {
                response.header(HttpHeaders.Authorization, "Bearer $token")
                respond(HttpStatusCode.OK)
            }
        }
        get("{id}") {
            val id = call.receive<Int>()

            val user = users.find { it.id == id }?: return@get call.respondText(
                "No user found",
                status = HttpStatusCode.NotFound
            )
            val userDto = LoginDto(user.username, user.hash)
            return@get call.respond(HttpStatusCode.OK, userDto)
        }
    }
}