package com.crateus.models

import java.util.*


val users = listOf(User(0, "a","a","a",Date(),"a"),
    User(1, "b","b","b",Date(),"b"),
    User(2, "c","c","c",Date(),"c"),
    User(3, "d","d","d",Date(),"d"),
)

data class User(val id: Int, val name: String, val username: String, val hash: String, val birthday: Date, val email: String)
