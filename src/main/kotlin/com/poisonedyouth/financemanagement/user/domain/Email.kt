package com.poisonedyouth.financemanagement.user.domain

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.poisonedyouth.financemanagement.failure.Failure
import java.util.regex.Pattern

private val validEmailPattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")

@JvmInline
public value class Email private constructor(public val value: String) {
    public companion object {
        public fun from(rawString: String): Either<Failure, Email> = either {
            ensure(validEmailPattern.matcher(rawString).matches()) {
                Failure.ValidationFailure("The given string '$rawString' is not a valid email.")
            }
            Email(rawString)
        }
    }
}
