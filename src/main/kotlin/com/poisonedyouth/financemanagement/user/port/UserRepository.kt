package com.poisonedyouth.financemanagement.user.port

import arrow.core.Either
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.user.domain.Email
import com.poisonedyouth.financemanagement.user.domain.User
import java.util.UUID

interface UserRepository {
    fun create(user: User): Either<Failure, User>
    fun update(user: User): Either<Failure, User>
    fun delete(userId: UUID): Either<Failure, Int>
    fun findById(userId: UUID): Either<Failure, User?>
    fun findByEmail(email: Email): Either<Failure, User?>
}
