package com.crateus.service

import com.password4j.Password

object HashManager {
    fun hashPassword(password: String): String = Password.hash(password).withBCrypt().result
    fun passwordsMatch(password: String, hash: String): Boolean = Password.check(password, hash).withBCrypt()
    fun passwordsDontMatch(password: String, hash: String): Boolean = !passwordsMatch(password, hash)
}