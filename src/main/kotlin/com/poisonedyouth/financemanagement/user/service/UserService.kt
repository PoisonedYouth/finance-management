package com.poisonedyouth.financemanagement.user.service

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.poisonedyouth.financemanagement.account.port.AccountUseCase
import com.poisonedyouth.financemanagement.common.Identity
import com.poisonedyouth.financemanagement.common.UUIDIdentity
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.failure.eval
import com.poisonedyouth.financemanagement.user.domain.Email
import com.poisonedyouth.financemanagement.user.domain.Name
import com.poisonedyouth.financemanagement.user.domain.User
import com.poisonedyouth.financemanagement.user.port.UserDto
import com.poisonedyouth.financemanagement.user.port.UserRepository
import com.poisonedyouth.financemanagement.user.port.UserUseCase
import org.slf4j.LoggerFactory
import java.util.UUID

class UserService(
    private val userRepository: UserRepository,
    private val accountUseCase: AccountUseCase
) : UserUseCase {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    override fun create(userDto: UserDto): Either<Failure, UserDto> = either {
        val user = validateAndCreateUser(userDto)
        val email = user.bind().email
        val existingUser = userRepository.findByEmail(email).bind()
        ensure(existingUser == null) {
            Failure.AlreadyExistFailure("User with email '${email.value}' already exists.")
        }

        val persistedUser = userRepository.create(user.bind()).bind()
        accountUseCase.triggerCreation(persistedUser.email)
        persistedUser.toUserDto().bind()
    }

    override fun update(userDto: UserDto): Either<Failure, UserDto> = either {
        val user = validateAndCreateUser(userDto)
        val updatedUser = userRepository.update(user.bind()).bind()
        updatedUser.toUserDto().bind()
    }

    override fun delete(userId: String): Either<Failure, Int> = either {
        val existingUserId = mapToUUID(userId).bind()
        val deletedAmount = userRepository.delete(existingUserId).bind()
        ensure(deletedAmount != 0) {
            Failure.NotFoundFailure("User with id '$userId' does not exist.")
        }
        deletedAmount
    }

    override fun findById(userId: String): Either<Failure, UserDto?> = either {
        val existingUserId = mapToUUID(userId).bind()
        userRepository.findById(existingUserId).bind()?.toUserDto()?.bind()
    }

    private fun mapToUUID(userId: String) = eval(logger) {
        UUID.fromString(userId)
    }.mapLeft { Failure.ValidationFailure(it.message) }

    private fun validateAndCreateUser(userDto: UserDto): Either<Failure, User> = either {
        User(
            userId = userDto.getOrCreateUUID().bind(),
            firstname = Name.from(userDto.firstname).bind(),
            lastname = Name.from(userDto.lastname).bind(),
            email = Email.from(userDto.email).bind()
        )
    }

    private fun UserDto.getOrCreateUUID(): Either<Failure, Identity> = eval(logger) {
        UUIDIdentity(UUID.fromString(this.userId ?: UUID.randomUUID().toString()))
    }

    private fun User.toUserDto(): Either<Failure, UserDto> = either {
        UserDto(
            userId = this@toUserDto.userId.getIdOrFailure().bind().toString(),
            firstname = this@toUserDto.firstname.value,
            lastname = this@toUserDto.lastname.value,
            email = this@toUserDto.email.value
        )
    }
}