package com.poisonedyouth.financemanagement.user.port

data class UserDto(
    val userId: String? = null,
    val firstname: String,
    val lastname: String,
    val email: String
)
