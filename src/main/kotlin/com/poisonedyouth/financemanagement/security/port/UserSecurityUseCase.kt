package com.poisonedyouth.financemanagement.security.port

import arrow.core.Either
import com.poisonedyouth.financemanagement.common.Identity
import com.poisonedyouth.financemanagement.failure.Failure

public interface UserSecurityUseCase {
    public fun triggerCreation(userId: Identity): Either<Failure, Unit>
    public fun update(userCredentialsDto: UserCredentialsDto): Either<Failure, Unit>
    public fun delete(userId: Identity): Either<Failure, Int>
    public fun validateCredentials(userCredentials: UserCredentialsDto): Either<Failure, Unit>
}
