package com.poisonedyouth.financemanagement.common

import arrow.core.Either
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.failure.eval
import org.slf4j.LoggerFactory
import java.util.UUID

private val logger = LoggerFactory.getLogger(UUIDIdentity::class.java)

data class UUIDIdentity(val id: UUID) {
    companion object {
        fun resolveFromString(value: String): Either<Failure, UUIDIdentity> {
            return eval(logger) {
                val uuid = UUID.fromString(value)
                UUIDIdentity(uuid)
            }.mapLeft {
                Failure.ValidationFailure(it.message)
            }
        }
    }
}
