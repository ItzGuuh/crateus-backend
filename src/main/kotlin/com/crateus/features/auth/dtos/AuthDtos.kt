package com.crateus.features.auth.dtos

import com.crateus.models.User
import com.crateus.service.HashManager
import com.crateus.utils.ToModel
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

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
    ) : ToModel<User> {

        override fun toModel(): User = User(
            name = name,
            username = username,
            hash = HashManager.hashPassword(password),
            birthday = LocalDateTime.ofInstant(Date(birthday).toInstant(), ZoneId.systemDefault()),
            email = email
        )
    }
}