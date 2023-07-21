package com.poisonedyouth.financemanagement.account.port

import arrow.core.Either
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.user.domain.Email

interface NotificationService {

    fun notify(notification: Notification): Either<Failure, Unit>
}

sealed interface Notification {

    data class EmailNotification(
        val email: Email
    ) : Notification
}
