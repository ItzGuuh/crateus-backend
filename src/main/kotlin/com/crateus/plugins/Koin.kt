package com.crateus.plugins

import com.crateus.database.DatabaseFactory
import com.crateus.database.DatabaseFactoryImpl
import com.crateus.service.AuthenticationService
import com.crateus.service.AuthenticationServiceImpl
import com.crateus.service.NoteService
import com.crateus.service.UserService
import org.koin.core.KoinApplication
import org.koin.dsl.module

val coreModule = module(createdAtStart = true) {
    single<DatabaseFactory> { DatabaseFactoryImpl() }
    single<AuthenticationService> { AuthenticationServiceImpl() }
    single { UserService() }
    single { NoteService() }
}

fun KoinApplication.loadModules() {
    modules(coreModule)
}