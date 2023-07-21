package com.poisonedyouth.financemanagement.user.adapter.persistence

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

private const val DEFAULT_VARCHAR_COLUMN_SIZE = 50

object UserTable : UUIDTable("user", "id") {
    val firstname = varchar("firstname", DEFAULT_VARCHAR_COLUMN_SIZE)
    val lastname = varchar("lastname", DEFAULT_VARCHAR_COLUMN_SIZE)
    val email = varchar("email", DEFAULT_VARCHAR_COLUMN_SIZE).uniqueIndex("unique_email")

    fun initTable() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(this@UserTable)
        }
    }
}
