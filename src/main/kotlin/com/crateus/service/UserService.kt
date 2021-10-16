package com.crateus.service

import com.crateus.database.dbQuery
import com.crateus.models.User
import com.crateus.models.Users
import com.crateus.utils.ResultHandler
import com.crateus.utils.runHandling
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.*

class UserService {
    suspend fun getAllUsers(): ResultHandler<List<User>> = dbQuery {
        Users.selectAll().map { toUser(it) }
    }

    suspend fun matchUserByEmail(email: String, password: String): ResultHandler<User> = runHandling {
        getUserByEmail(email).let { return when (it) {
            is ResultHandler.Success -> if (HashManager.passwordsMatch(password = password, hash = it.value.hash)) it
                                else ResultHandler.Failure(throwable = Exception("No user found, username or password incorrect!"))
            is ResultHandler.Failure -> it
        } }
    }

    private suspend fun getUserByEmail(email: String): ResultHandler<User> =
        dbQuery {
            Users.select {
                (Users.email eq email)
            }.mapNotNull { toUser(it) }
                .single()
        }


    suspend fun getUserById(id: UUID): ResultHandler<User> =
        dbQuery {
            Users.select {
                (Users.id eq id)
            }.mapNotNull { toUser(it) }
                .single()
        }


    suspend fun insertUser(user: User): ResultHandler<EntityID<UUID>> = dbQuery {
        return@dbQuery Users.insertAndGetId {
            it[name] = user.name
            it[username] = user.username
            it[hash] = user.hash
            it[birthday] = user.birthday
            it[email] = user.email
        }
    }


    private fun toUser(row: ResultRow): User = User(
        name = row[Users.name],
        username = row[Users.username],
        hash = row[Users.hash],
        birthday = row[Users.birthday],
        email = row[Users.email],
    ).apply {
        id = row[Users.id].value
        createdAt = row[Users.createdAt]
    }
}