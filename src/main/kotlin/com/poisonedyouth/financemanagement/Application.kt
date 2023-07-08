package com.poisonedyouth.financemanagement

import com.poisonedyouth.financemanagement.plugins.configureDatabases
import com.poisonedyouth.financemanagement.plugins.configureRouting
import com.poisonedyouth.financemanagement.plugins.configureSecurity
import com.poisonedyouth.financemanagement.plugins.configureSerialization
import io.ktor.server.application.Application

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function.
fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureDatabases()
    configureRouting()
}
