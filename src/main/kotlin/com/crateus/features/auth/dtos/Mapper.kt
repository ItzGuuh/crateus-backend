package com.crateus.features.auth.dtos

import com.crateus.models.User
import com.crateus.service.HashManager
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object Mapper {

    fun dtoToUser(dto: AuthDtos.UserDto): User = User(
        name = dto.name,
        username = dto.username,
        hash = HashManager.hashPassword(dto.password),
        birthday = LocalDateTime.ofInstant(Date(dto.birthday).toInstant(), ZoneId.systemDefault()),
        email = dto.email
    )
}