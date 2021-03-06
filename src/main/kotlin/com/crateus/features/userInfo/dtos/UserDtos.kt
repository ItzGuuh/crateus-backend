package com.crateus.features.userInfo.dtos

import kotlinx.serialization.Serializable

object UserDtos {
    @Serializable
    data class UserDto(
        val name: String,
        val username: String,
        val birthday: Long,
        val email: String,
    )
}