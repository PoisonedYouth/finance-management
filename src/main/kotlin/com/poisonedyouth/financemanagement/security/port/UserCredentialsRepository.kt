package com.poisonedyouth.financemanagement.security.port

import arrow.core.Either
import com.poisonedyouth.financemanagement.common.Identity
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.security.domain.UserCredentials

public interface UserCredentialsRepository {
    public fun create(userCredentials: UserCredentials): Either<Failure, UserCredentials>
    public fun update(userCredentials: UserCredentials): Either<Failure, UserCredentials>
    public fun delete(userId: Identity): Either<Failure, Int>
    public fun findById(userId: Identity): Either<Failure, UserCredentials?>
}
