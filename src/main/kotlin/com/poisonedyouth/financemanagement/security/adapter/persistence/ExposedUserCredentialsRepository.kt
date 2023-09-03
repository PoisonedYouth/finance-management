package com.poisonedyouth.financemanagement.security.adapter.persistence

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.poisonedyouth.financemanagement.common.Identity
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.failure.eval
import com.poisonedyouth.financemanagement.security.domain.UserCredentials
import com.poisonedyouth.financemanagement.security.port.UserCredentialsRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory

public class ExposedUserCredentialsRepository : UserCredentialsRepository {
    private val logger = LoggerFactory.getLogger(ExposedUserCredentialsRepository::class.java)

    override fun create(userCredentials: UserCredentials): Either<Failure, UserCredentials> = transaction {
        eval(logger) {
            UserCredentialsTable.insert {
                it[userId] = userCredentials.userId.id
                it[password] = userCredentials.password
            }
            userCredentials
        }
    }

    override fun update(userCredentials: UserCredentials): Either<Failure, UserCredentials> = transaction {
        val updateResult = eval(logger) {
            UserCredentialsTable.update({ UserCredentialsTable.userId eq userCredentials.userId.id }) {
                it[password] = userCredentials.password
            }
        }
        either {
            ensure(updateResult.bind() == 1) {
                Failure.NotFoundFailure("The user credentials for userId '${userCredentials.userId.id}' does not exist.")
            }
            userCredentials
        }
    }

    override fun delete(userId: Identity): Either<Failure, Int> = transaction {
        eval(logger) {
            UserCredentialsTable.deleteWhere { UserCredentialsTable.userId eq userId.id }
        }
    }

    override fun findById(userId: Identity): Either<Failure, UserCredentials?> = transaction {
        either {
            eval(logger) {
                UserCredentialsTable.select { UserCredentialsTable.userId eq userId.id }.firstOrNull()
            }.bind()?.let {
                UserCredentials(
                    userId = Identity(it[UserCredentialsTable.userId]),
                    password = it[UserCredentialsTable.password]
                )
            }
        }
    }
}
