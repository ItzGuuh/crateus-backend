package com.crateus.utils

import com.crateus.service.SecurityVariables
import io.ktor.application.*

fun Application.getSecurityVariables() = SecurityVariables(
    secret = this.environment.config.property("jwt.secret").getString(),
    issuer = this.environment.config.property("jwt.issuer").getString(),
    audience = this.environment.config.property("jwt.audience").getString(),
    realm = this.environment.config.property("jwt.realm").getString()
)

sealed class ResultHandler<out T> {
    class Success<out T>(val value: T) : ResultHandler<T>()
    class Failure(val throwable: Throwable?=null, val message: String) : ResultHandler<Nothing>()
}

inline fun <R> runHandling(block: () -> R): ResultHandler<R> = try {
        ResultHandler.Success(block())
    } catch (e: Throwable) {
        ResultHandler.Failure(e, e.message?: e.localizedMessage)
    }
