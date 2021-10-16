package com.crateus.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.crateus.utils.getSecurityVariables
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*


fun Application.configureSecurity() {
    authentication {
        jwt {
            realm = this@configureSecurity.getSecurityVariables().realm
            verifier(buildJwtVerifier(
                this@configureSecurity.getSecurityVariables().secret,
                this@configureSecurity.getSecurityVariables().audience,
                this@configureSecurity.getSecurityVariables().issuer
            ))
            validate { credential -> validateCredentials(credential) }
        }
    }
}

private fun buildJwtVerifier(
    secret: String,
    audience: String,
    issuer: String
) = JWT
    .require(Algorithm.HMAC256(secret))
    .withAudience(audience)
    .withIssuer(issuer)
    .build()

private fun validateCredentials(credential: JWTCredential) =
    if (credential.payload.getClaim("username").asString() != "") JWTPrincipal(credential.payload)
    else null
