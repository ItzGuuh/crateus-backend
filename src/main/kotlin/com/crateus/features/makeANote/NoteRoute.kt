package com.crateus.features.makeANote

import com.crateus.features.makeANote.dtos.NoteDtos
import com.crateus.service.NoteService
import com.crateus.utils.ResultHandler
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.noteRoutes() {
    val noteService: NoteService by inject()
    routing {
        authenticate {
            noteRoute(noteService)
        }
    }
}

fun Route.noteRoute(noteService: NoteService) {
    route("/note") {
        put("/create") {
            call.run {
                val note = receive<NoteDtos.NoteDto>().toModel()
                when (val result = noteService.insertNote(note)) {
                    is ResultHandler.Failure -> {
                        application.log.error(result.message, result.throwable)
                        respondText(text = result.message, status = HttpStatusCode.InternalServerError)
                    }
                    is ResultHandler.Success -> call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}