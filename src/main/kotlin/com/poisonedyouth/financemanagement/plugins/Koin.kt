package com.poisonedyouth.financemanagement.plugins

import com.poisonedyouth.financemanagement.account.port.AccountUseCase
import com.poisonedyouth.financemanagement.account.service.AccountService
import com.poisonedyouth.financemanagement.notification.port.NotificationService
import com.poisonedyouth.financemanagement.notification.service.UserCredentialsService
import com.poisonedyouth.financemanagement.security.port.UserSecurityUseCase
import  com.poisonedyouth.financemanagement.security.service.UserSecurityService
import com.poisonedyouth.financemanagement.user.adapter.persistence.ExposedUserRepository
import com.poisonedyouth.financemanagement.user.port.UserRepository
import com.poisonedyouth.financemanagement.user.port.UserUseCase
import com.poisonedyouth.financemanagement.user.service.UserService
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

public fun KoinApplication.defaultModule(): KoinApplication = modules(defaultModule)
public val defaultModule: Module = module {
    singleOf(::ExposedUserRepository) bind UserRepository::class
    singleOf(::UserService) bind UserUseCase::class
    singleOf(::AccountService) bind AccountUseCase::class
    singleOf(::UserSecurityService) bind UserSecurityUseCase::class
    singleOf(::UserCredentialsService) bind NotificationService::class
}

public fun Application.configureDependencyInjection() {
    // Install Ktor features
    install(Koin) {
        slf4jLogger()
        modules(defaultModule)
    }
}
