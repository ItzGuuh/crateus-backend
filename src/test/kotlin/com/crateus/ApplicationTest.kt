package com.crateus

import io.ktor.auth.*
import io.ktor.util.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.jwt.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.crateus.plugins.*

class ApplicationTest {
    @Test
    fun testRoot() {
        val secVar = SecurityVariables(secret = "", issuer = "", audience = "")
        withTestApplication({ configureRouting(secVar) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hello World!", response.content)
            }
        }
    }
}