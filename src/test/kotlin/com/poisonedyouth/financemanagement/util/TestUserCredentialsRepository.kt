package com.poisonedyouth.financemanagement.util

import arrow.core.Either
import arrow.core.raise.either
import com.poisonedyouth.financemanagement.common.Identity
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.security.domain.UserCredentials
import com.poisonedyouth.financemanagement.security.port.UserCredentialsRepository

class TestUserCredentialsRepository : UserCredentialsRepository {
    override fun create(userCredentials: UserCredentials): Either<Failure, UserCredentials> = either {
        println("created")
        userCredentials
    }

    override fun update(userCredentials: UserCredentials): Either<Failure, UserCredentials> {
        TODO("Not yet implemented")
    }

    override fun delete(userId: Identity): Either<Failure, Int> {
        TODO("Not yet implemented")
    }

    override fun findById(userId: Identity): Either<Failure, UserCredentials?> {
        TODO("Not yet implemented")
    }
}
