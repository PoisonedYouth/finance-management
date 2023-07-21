package com.poisonedyouth.financemanagement.user.domain

import com.poisonedyouth.financemanagement.common.Identity
import com.poisonedyouth.financemanagement.common.NoIdentity

data class User(
    val userId: Identity = NoIdentity,
    val firstname: Name,
    val lastname: Name,
    val email: Email
)
