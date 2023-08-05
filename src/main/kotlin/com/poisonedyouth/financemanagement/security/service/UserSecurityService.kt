package com.poisonedyouth.financemanagement.security.service

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import com.poisonedyouth.financemanagement.common.Identity
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.notification.domain.Notification
import com.poisonedyouth.financemanagement.notification.port.NotificationService
import com.poisonedyouth.financemanagement.security.domain.UserCredentials
import com.poisonedyouth.financemanagement.security.port.UserCredentialsDto
import com.poisonedyouth.financemanagement.security.port.UserCredentialsRepository
import com.poisonedyouth.financemanagement.security.port.UserSecurityUseCase
import com.poisonedyouth.financemanagement.security.port.toUserCredentials

public class UserSecurityService(
    private val notificationService: NotificationService,
    private val userCredentialsRepository: UserCredentialsRepository
) : UserSecurityUseCase {
    override fun triggerCreation(userId: Identity): Either<Failure, Unit> = either {
        val password = PasswordManager.createRandomPassword()
        userCredentialsRepository.create(
            UserCredentials(
                userId = userId,
                password = password
            )
        ).bind()
        notificationService.notify(
            Notification.UserCredentialsNotification(
                userId = userId,
                password = password
            )
        ).bind()
    }

    override fun update(userCredentialsDto: UserCredentialsDto): Either<Failure, Unit> {
        TODO("Not yet implemented")
    }

    override fun delete(userId: Identity): Either<Failure, Int> {
        TODO("Not yet implemented")
    }

    override fun validateCredentials(userCredentials: UserCredentialsDto): Either<Failure, Unit> = either {
        val userId = Identity.resolveFromString(userCredentials.userId).bind()
        val existingUserCredentials = userCredentialsRepository.findById(userId).bind()
        ensureNotNull(existingUserCredentials) {
            Failure.NotFoundFailure("For userId ${userCredentials.userId} no credentials found.")
        }
        PasswordManager.validateCredentials(userCredentials.toUserCredentials().bind(), existingUserCredentials).bind()
    }
}
