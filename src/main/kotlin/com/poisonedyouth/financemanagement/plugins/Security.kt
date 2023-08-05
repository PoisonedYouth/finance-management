package com.poisonedyouth.financemanagement.plugins

import arrow.core.Either
import arrow.core.raise.either
import com.poisonedyouth.financemanagement.security.port.UserCredentialsDto
import com.poisonedyouth.financemanagement.security.port.UserSecurityUseCase
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.basic
import org.koin.ktor.ext.inject

public fun Application.configureSecurity() {
    val userSecurityUseCase by inject<UserSecurityUseCase>()

    install(Authentication) {
        basic("basic-auth") {
            realm = "basic-auth-realm"
            validate { credentials ->
                val result = either {
                    userSecurityUseCase.validateCredentials(
                        UserCredentialsDto(
                            credentials.name,
                            credentials.password
                        )
                    ).bind()
                }
                when (result) {
                    is Either.Right -> UserIdPrincipal(credentials.name)
                    else -> null
                }

            }
        }
    }
}
