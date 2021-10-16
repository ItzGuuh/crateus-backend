package com.crateus.models

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*

object Notes: UUIDTable() {
    val title: Column<String> = varchar("title", 100)
    val body: Column<String> = varchar("body", 2500)
    val author: Column<String> = varchar("author", 20).references(Users.username)
    val upVotes: Column<Int> = integer("upvote").default(0)
    val downVotes: Column<Int> = integer("downvote").default(0)
    val createdAt: Column<LocalDateTime> = datetime("created_at").defaultExpression(CurrentDateTime())
    val updatedAt: Column<LocalDateTime> = datetime("updated_at")
}



data class Note(val title: String,
                val body: String,
                val author: String,
                val upVotes: Int = 0,
                val downVotes: Int = 0
){
    lateinit var id: UUID
    lateinit var createdAt: LocalDateTime
    val updatedAt: LocalDateTime by lazy { LocalDateTime.now() }
}
