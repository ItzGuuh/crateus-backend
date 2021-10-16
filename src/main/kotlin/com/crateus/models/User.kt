package com.crateus.models

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*


object Users : UUIDTable() {
    val name: Column<String> = varchar("name", 30)
    val username: Column<String> = varchar("username", 20).uniqueIndex()
    val email: Column<String> = varchar("email", 100)
    val hash: Column<String> = varchar("hash", 100)
    val birthday: Column<LocalDateTime> = datetime("birthday")
    val createdAt: Column<LocalDateTime> = datetime("created_at").defaultExpression(CurrentDateTime())
}



data class User(val name : String,
                val username: String,
                val hash: String,
                val birthday: LocalDateTime,
                val email: String,
){
    lateinit var id: UUID
    lateinit var createdAt: LocalDateTime
}
