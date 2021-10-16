package com.crateus.features.makeANote.dtos

import com.crateus.models.Note
import com.crateus.utils.ToModel
import kotlinx.serialization.Serializable


object NoteDtos {

    @Serializable
    data class NoteDto(
        val title: String,
        val body: String,
        val author: String
    ) : ToModel<Note> {
        override fun toModel(): Note = Note(
            title = title,
            body = body,
            author = author
        )
    }
}