package com.crateus


import com.crateus.database.configureDatabase
import com.crateus.plugins.configureRouting
import com.crateus.plugins.configureSecurity
import com.crateus.plugins.loadModules
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.features.DataConversion
import io.ktor.serialization.*
import io.ktor.util.converters.*
import org.koin.ktor.ext.Koin
import org.koin.logger.slf4jLogger
import java.text.SimpleDateFormat
import java.util.*

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    log.info("Initializing server...")
    installFeatures()
    configure()
}

private fun Application.configure() {
    log.info("Setting up database...")
    configureDatabase()

    log.info("Configuring authentication")
    configureSecurity()

    log.info("Configuring routes")
    configureRouting()
}

private fun Application.installFeatures() {
    log.info("Installing features")
    install(ContentNegotiation) {
        json()
    }
    install(DataConversion) {
        convert<Date> { // this: DelegatingConversionService
            val format = SimpleDateFormat.getInstance()

            decode { values, _ -> // converter: (values: List<String>, type: Type) -> Any?
                values.singleOrNull()?.let { format.parse(it) }
            }

            encode { value -> // converter: (value: Any?) -> List<String>
                when (value) {
                    null -> listOf()
                    is Date -> listOf(SimpleDateFormat.getInstance().format(value))
                    else -> throw DataConversionException("Cannot convert $value as Date")
                }
            }
        }
    }
    install(Koin) {
        slf4jLogger()
        loadModules()
    }
}