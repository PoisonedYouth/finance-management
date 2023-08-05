package com.poisonedyouth.financemanagement.security.port

import arrow.core.Either
import arrow.core.raise.either
import com.poisonedyouth.financemanagement.common.Identity
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.security.domain.UserCredentials

public data class UserCredentialsDto(
    val userId: String,
    val password: String
)

public fun UserCredentialsDto.toUserCredentials(): Either<Failure, UserCredentials> = either {
    UserCredentials(
        userId = Identity.resolveFromString(this@toUserCredentials.userId).bind(),
        password = this@toUserCredentials.password
    )
}
