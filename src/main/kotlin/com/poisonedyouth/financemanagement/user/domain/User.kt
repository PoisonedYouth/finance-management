package com.poisonedyouth.financemanagement.user.domain

import com.poisonedyouth.financemanagement.common.Identity

data class User(
    val userId: Identity,
    val firstname: Name,
    val lastname: Name,
    val email: Email
)

data class NewUser(
    val firstname: Name,
    val lastname: Name,
    val email: Email
)
