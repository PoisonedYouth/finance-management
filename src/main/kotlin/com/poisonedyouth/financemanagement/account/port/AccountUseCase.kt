package com.poisonedyouth.financemanagement.account.port

import arrow.core.Either
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.user.domain.Email

interface AccountUseCase {

    fun triggerCreation(email: Email): Either<Failure, Unit>
}
