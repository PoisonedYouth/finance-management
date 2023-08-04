package com.poisonedyouth.financemanagement.util

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.poisonedyouth.financemanagement.common.Identity
import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.user.domain.Email
import com.poisonedyouth.financemanagement.user.domain.Name
import com.poisonedyouth.financemanagement.user.domain.NewUser
import com.poisonedyouth.financemanagement.user.domain.User
import com.poisonedyouth.financemanagement.user.port.UserRepository
import io.kotest.assertions.arrow.core.shouldBeRight
import java.util.UUID

val defaultUserId: Identity = Identity(UUID.fromString("85d374d2-440c-4db7-8db5-9f70e31b415e"))
val defaultUserEmail: Email = Email.from("john.doe@mail.com").shouldBeRight()
val duplicateUserEmail: Email = Email.from("not.doe@mail.com").shouldBeRight()
val notExistingUserId: Identity = Identity(UUID.fromString("95d374d2-440c-4db7-8db5-9f70e31b415e"))

val defaultUser: User = User(
    userId = defaultUserId,
    firstname = Name.from("John").shouldBeRight(),
    lastname = Name.from("Doe").shouldBeRight(),
    email = defaultUserEmail
)

class TestUserRepository : UserRepository {
    override fun create(user: NewUser): Either<Failure, User> = either {
        ensure(user.email.value == "john.doe@mail.com") {
            Failure.GenericFailure(RuntimeException("Cannot persist '${user.email.value}'"))
        }
        User(
            userId = defaultUserId,
            firstname = user.firstname,
            lastname = user.lastname,
            email = user.email
        )
    }

    override fun update(user: User): Either<Failure, User> = either {
        ensure(user.email.value == "john.doe@mail.com") {
            Failure.GenericFailure(RuntimeException("Cannot update '${user.email.value}'"))
        }
        user
    }

    override fun delete(userId: Identity): Either<Failure, Int> = either {
        ensure(userId == defaultUserId || userId == notExistingUserId) {
            Failure.GenericFailure(RuntimeException("Cannot delete '${userId.id}'"))
        }
        if (userId == notExistingUserId) 0 else 1
    }

    override fun findById(userId: Identity): Either<Failure, User?> = either {
        ensure(userId == defaultUserId || userId == notExistingUserId) {
            Failure.GenericFailure(RuntimeException("Cannot find '${userId.id}'"))
        }
        if (userId == notExistingUserId) null else defaultUser
    }

    override fun findByEmail(email: Email): Either<Failure, User?> = either {
        if (email == duplicateUserEmail) defaultUser else null
    }
}
