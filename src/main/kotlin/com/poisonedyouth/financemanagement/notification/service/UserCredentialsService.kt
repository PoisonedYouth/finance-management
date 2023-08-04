package com.poisonedyouth.financemanagement.notification.service

import arrow.core.Either
import arrow.core.raise.either
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.notification.domain.Notification
import com.poisonedyouth.financemanagement.notification.port.NotificationService

public class UserCredentialsService : NotificationService {
    override fun notify(notification: Notification): Either<Failure, Unit> = either {
        println("New UserCredentials created: $notification")
    }
}
