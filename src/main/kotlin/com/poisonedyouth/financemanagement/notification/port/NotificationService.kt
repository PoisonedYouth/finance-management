package com.poisonedyouth.financemanagement.notification.port

import arrow.core.Either
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.notification.domain.Notification

public interface NotificationService {

    public fun notify(notification: Notification): Either<Failure, Unit>
}
