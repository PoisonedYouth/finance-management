package com.poisonedyouth.financemanagement.notification.service

import arrow.core.Either
import arrow.core.raise.either
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.notification.domain.Notification
import com.poisonedyouth.financemanagement.notification.port.NotificationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class UserCredentialsService : NotificationService {
    private val logger: Logger = LoggerFactory.getLogger(UserCredentialsService::class.java)

    override fun notify(notification: Notification): Either<Failure, Unit> = either {
        logger.info("New UserCredentials created: $notification")
    }
}
