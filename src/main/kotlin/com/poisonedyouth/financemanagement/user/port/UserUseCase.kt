package com.poisonedyouth.financemanagement.user.port

import arrow.core.Either
import com.poisonedyouth.financemanagement.failure.Failure

interface UserUseCase {
    fun create(userDto: NewUserDto): Either<Failure, UserDto>
    fun update(userDto: UserDto): Either<Failure, UserDto>
    fun delete(userId: String): Either<Failure, Int>
    fun findById(userId: String): Either<Failure, UserDto?>
}
