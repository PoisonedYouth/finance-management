package com.poisonedyouth.financemanagement.common

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.poisonedyouth.financemanagement.failure.Failure
import java.util.UUID

sealed interface Identity {
    fun getIdOrNull(): UUID? {
        return when (this) {
            is UUIDIdentity -> this.id
            NoIdentity -> null
        }
    }

    fun getIdOrFailure(): Either<Failure, UUID> = either {
        ensure(this@Identity is UUIDIdentity) {
            Failure.ValidationFailure("The id is not yet set.")
        }
        this@Identity.id
    }
}

data object NoIdentity : Identity

data class UUIDIdentity(val id: UUID) : Identity
