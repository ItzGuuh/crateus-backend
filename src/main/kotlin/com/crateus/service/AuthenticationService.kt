package com.crateus.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.crateus.utils.getSecurityVariables
import io.ktor.application.*
import java.util.*

interface AuthenticationService {
    fun createJwt(application: Application, usernameClaim: String, isAdmin: Boolean=false): String
}

const val tokenExpirationHour = 6

class AuthenticationServiceImpl: AuthenticationService {
    override fun createJwt(application: Application, usernameClaim: String, isAdmin: Boolean): String = JWT.create()
        .withAudience(application.getSecurityVariables().audience)
        .withIssuer(application.getSecurityVariables().issuer)
        .withExpiresAt(getExpirationDateToken())
        .withIssuedAt(Date())
        .withClaim("username", usernameClaim)
        .withClaim("isAdmin", isAdmin)
        .sign(Algorithm.HMAC256(application.getSecurityVariables().secret))

    private fun getExpirationDateToken(): Date = Calendar.getInstance().apply { time = Date(); add(Calendar.HOUR_OF_DAY, tokenExpirationHour) }.time
}


data class SecurityVariables(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String
)