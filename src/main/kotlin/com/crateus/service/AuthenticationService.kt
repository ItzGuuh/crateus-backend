package com.crateus.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.crateus.utils.getSecurityVariables
import io.ktor.application.*
import java.util.*

interface AuthenticationService {
    fun createJwt(application: Application, claim: String): String
}

class AuthenticationServiceImpl: AuthenticationService {
    override fun createJwt(application: Application, claim: String): String = JWT.create()
        .withAudience(application.getSecurityVariables().audience)
        .withIssuer(application.getSecurityVariables().issuer)
        .withClaim("username", claim)
        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
        .sign(Algorithm.HMAC256(application.getSecurityVariables().secret))
}

data class SecurityVariables(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String
)