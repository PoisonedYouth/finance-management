package com.poisonedyouth.financemanagement.notification.domain

import com.poisonedyouth.financemanagement.common.Identity
import com.poisonedyouth.financemanagement.user.domain.Email

public sealed interface Notification {

    public data class EmailNotification(
        val email: Email
    ) : Notification

    public data class UserCredentialsNotification(
        val userId: Identity,
        val password: String
    ) : Notification
}
