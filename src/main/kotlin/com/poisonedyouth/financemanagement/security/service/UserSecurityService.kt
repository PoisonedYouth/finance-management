package com.poisonedyouth.financemanagement.security.service

import arrow.core.Either
import arrow.core.raise.either
import com.poisonedyouth.financemanagement.common.Identity
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.notification.domain.Notification
import com.poisonedyouth.financemanagement.notification.port.NotificationService
import com.poisonedyouth.financemanagement.security.domain.UserCredentials
import com.poisonedyouth.financemanagement.security.port.UserCredentialsDto
import com.poisonedyouth.financemanagement.security.port.UserSecurityUseCase

public class UserSecurityService(
    private val notificationService: NotificationService
) : UserSecurityUseCase {
    override fun triggerCreation(userId: Identity): Either<Failure, Unit> = either {
        notificationService.notify(
            Notification.UserCredentialsNotification(
                userId = userId,
                password = "passw0rd"
            )
        ).bind()
    }

    override fun update(userCredentialsDto: UserCredentialsDto): Either<Failure, Unit> {
        TODO("Not yet implemented")
    }

    override fun delete(userId: Identity): Either<Failure, Int> {
        TODO("Not yet implemented")
    }

    override fun findById(userId: Identity): Either<Failure, UserCredentials> {
        TODO("Not yet implemented")
    }
}
