package com.poisonedyouth.financemanagement.user.port

import arrow.core.Either
import com.poisonedyouth.financemanagement.failure.Failure

interface UserUseCase {
    fun create(userDto: NewUserDto): Either<Failure, String>
    fun update(userDto: UserDto): Either<Failure, Unit>
    fun delete(userId: String): Either<Failure, Int>
    fun findById(userId: String): Either<Failure, UserDto?>
}
