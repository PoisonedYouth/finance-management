package com.poisonedyouth.financemanagement.user.domain

import com.poisonedyouth.financemanagement.common.UUIDIdentity

data class User(
    val userId: UUIDIdentity,
    val firstname: Name,
    val lastname: Name,
    val email: Email
)

data class NewUser(
    val firstname: Name,
    val lastname: Name,
    val email: Email
)
