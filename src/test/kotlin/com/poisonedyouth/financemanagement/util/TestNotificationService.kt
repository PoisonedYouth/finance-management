package com.poisonedyouth.financemanagement.util

import arrow.core.Either
import arrow.core.raise.either
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.notification.domain.Notification
import com.poisonedyouth.financemanagement.notification.port.NotificationService

class TestNotificationService : NotificationService {
    override fun notify(notification: Notification): Either<Failure, Unit> = either {
        println("Notify $notification")
    }
}
