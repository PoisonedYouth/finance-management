package com.poisonedyouth.financemanagement.security.adapter.persistence

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

private const val DEFAULT_VARCHAR_COLUMN_SIZE = 100

public object UserCredentialsTable : UUIDTable("user_credentials", "id") {
    public val userId: Column<UUID> = uuid("userId").uniqueIndex("unique_userId")
    public val password: Column<String> = varchar("password", DEFAULT_VARCHAR_COLUMN_SIZE)

    public fun initTable() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(this@UserCredentialsTable)
        }
    }
}
