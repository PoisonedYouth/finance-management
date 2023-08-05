package com.poisonedyouth.financemanagement.security.domain

import com.poisonedyouth.financemanagement.common.Identity

public data class UserCredentials(
    val userId: Identity,
    val password: String
)
