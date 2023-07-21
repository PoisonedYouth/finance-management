package com.poisonedyouth.financemanagement.util

import arrow.core.raise.either
import com.poisonedyouth.financemanagement.user.domain.Email
import com.poisonedyouth.financemanagement.user.domain.Name
import com.poisonedyouth.financemanagement.user.domain.User
import com.poisonedyouth.financemanagement.user.port.UserDto
import io.kotest.assertions.arrow.core.shouldBeRight
import kotlin.random.Random

fun createRandomDefaultUser(): User {
    val randomSuffix = Random.nextInt()
    return either {
        User(
            firstname = Name.from("John").bind(),
            lastname = Name.from("Doe").bind(),
            email = Email.from("John.Doe-$randomSuffix@mail.com").bind()
        )
    }.shouldBeRight()
}

fun createRandomDefaultUserDto(): UserDto {
    val randomSuffix = Random.nextInt()
    return UserDto(
        userId = defaultUserId.toString(),
        firstname = "John",
        lastname = "Doe",
        email = "john.doe-$randomSuffix@mail.com"
    )
}
