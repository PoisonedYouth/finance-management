package com.poisonedyouth.financemanagement.failure

import arrow.core.Either
import org.slf4j.Logger

sealed interface Failure {
    val message: String

    data class ValidationFailure(override val message: String) : Failure

    data class NotFoundFailure(override val message: String) : Failure

    data class AlreadyExistFailure(override val message: String) : Failure

    data class GenericFailure(val e: Throwable) : Failure {
        override val message: String = e.message ?: "Unknown error occurred"
    }
}

fun <T> eval(logger: Logger, exec: () -> T): Either<Failure, T> {
    return Either.catch {
        exec()
    }.mapLeft {
        logger.error("Failed to execute operation because of - ${it.message}")
        Failure.GenericFailure(it)
    }
}
