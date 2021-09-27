package com.crateus.features.auth.dtos

import kotlinx.serialization.Serializable

object AuthDtos {
    @Serializable
    data class LoginDto(val username: String, val password: String)

    @Serializable
    data class UserDto(
        val name: String,
        val username: String,
        val password: String,
        val birthday: Long,
        val email: String,
    )
}