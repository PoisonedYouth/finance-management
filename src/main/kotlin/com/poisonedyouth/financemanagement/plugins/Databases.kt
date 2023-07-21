package com.poisonedyouth.financemanagement.plugins

import com.poisonedyouth.financemanagement.user.adapter.persistence.UserTable
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val databaseConfig = environment.config.config("ktor.database")
    val database = Database.connect(
        url = databaseConfig.property("url").getString(),
        user = databaseConfig.property("user").getString(),
        driver = databaseConfig.property("driver").getString(),
        password = databaseConfig.property("password").getString()
    )

    transaction(database) {
        UserTable.initTable()
    }
}
