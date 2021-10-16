package com.crateus.service

import com.crateus.database.dbQuery
import com.crateus.models.Note
import com.crateus.models.Notes
import com.crateus.models.Users
import com.crateus.utils.ResultHandler
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll

class NoteService {
    suspend fun getAllNotes(): ResultHandler<Unit> = dbQuery {
        Notes.selectAll().map { toNote(it) }
    }

    suspend fun insertNote(note: Note)= dbQuery {
        Notes.insertAndGetId {
            it[title] = note.title
            it[body] = note.body
            it[author] = note.author
            it[upVotes] = note.upVotes
            it[downVotes] = note.downVotes
            it[updatedAt] = note.updatedAt
        }
    }

    private fun toNote(row: ResultRow): Note = Note(
        title = row[Notes.title],
        body = row[Notes.body],
        author = row[Notes.author],
        upVotes = row[Notes.upVotes],
        downVotes = row[Notes.downVotes]
    ).apply {
        id = row[Notes.id].value
        createdAt = row[Users.createdAt]
    }
}
