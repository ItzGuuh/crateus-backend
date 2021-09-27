package com.crateus.repos

import com.crateus.database.dbQuery
import com.crateus.models.User
import com.crateus.models.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.*

class UserRepository {
    suspend fun getAllUsers(): List<User> = dbQuery {
        Users.selectAll().map { toUser(it) }
    }

    suspend fun getUserByEmail(email: String): User? = dbQuery {
        Users.select {
            (Users.email eq email)
        }.mapNotNull { toUser(it) }
            .singleOrNull()
    }

    suspend fun getUserById(id: UUID): User? = dbQuery {
        Users.select {
            (Users.id eq id)
        }.mapNotNull { toUser(it) }
            .singleOrNull()
    }

    suspend fun insertUser(user: User) = dbQuery {
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