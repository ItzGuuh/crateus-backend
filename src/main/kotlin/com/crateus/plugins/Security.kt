package com.crateus.plugins

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.application.*
import java.util.concurrent.TimeUnit

data class SecurityVariables(
    val secret: String,
    val issuer: String,
    val audience: String,
)

fun Application.configureSecurity(): SecurityVariables {
/*  val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()
*/
    val secret = "secret"
    val issuer = "http://0.0.0.0:8080/"
    val audience = "http://0.0.0.0:8080/hello"
    val myRealm = "Access to 'hello'"

    authentication {
        jwt {
            realm = myRealm
            verifier(buildJwtVerifier(secret, audience, issuer))
            validate { credential -> validateCredentials(credential)}
        }
    }
    return SecurityVariables(secret, issuer, audience)
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
