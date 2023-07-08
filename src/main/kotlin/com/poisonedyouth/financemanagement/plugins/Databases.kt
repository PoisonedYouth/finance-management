package com.poisonedyouth.financemanagement.plugins

import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val databaseConfig = environment.config.config("ktor.database")
    Database.connect(
        url = databaseConfig.property("url").getString(),
        user = databaseConfig.property("user").getString(),
        driver = databaseConfig.property("driver").getString(),
        password = databaseConfig.property("password").getString()
    )
}
