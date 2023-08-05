package com.poisonedyouth.financemanagement.security.service

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.security.domain.UserCredentials
import kotlin.random.Random

public object PasswordManager {

    public fun validateCredentials(
        userCredentials: UserCredentials,
        storedUserCredentials: UserCredentials
    ): Either<Failure, Unit> = either {
        ensure(userCredentials.userId == storedUserCredentials.userId && userCredentials.password == storedUserCredentials.password) {
            Failure.AuthenticationFailure("The credentials for the userId '${userCredentials.userId}' are incorrect.")
        }
    }

    public fun createRandomPassword(length: Int = 16): String {
        val characterSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

        val random = Random(System.nanoTime())
        val password = StringBuilder()

        repeat(length) {
            val rIndex = random.nextInt(characterSet.length)
            password.append(characterSet[rIndex])
        }

        return password.toString()
    }
}
