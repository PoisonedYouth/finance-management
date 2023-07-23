package com.poisonedyouth.financemanagement.user.port

data class UserDto(
    val userId: String,
    val firstname: String,
    val lastname: String,
    val email: String
)

data class NewUserDto(
    val firstname: String,
    val lastname: String,
    val email: String
)
