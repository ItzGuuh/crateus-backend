package com.crateus.utils

import com.crateus.service.SecurityVariables
import io.ktor.application.*

fun Application.getSecurityVariables() = SecurityVariables(
    secret = this.environment.config.property("jwt.secret").getString(),
    issuer = this.environment.config.property("jwt.issuer").getString(),
    audience = this.environment.config.property("jwt.audience").getString(),
    realm = this.environment.config.property("jwt.realm").getString()
)