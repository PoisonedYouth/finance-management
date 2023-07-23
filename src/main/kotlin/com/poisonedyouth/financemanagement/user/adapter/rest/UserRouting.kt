package com.poisonedyouth.financemanagement.user.adapter.rest

import com.poisonedyouth.financemanagement.failure.Failure
import com.poisonedyouth.financemanagement.user.port.NewUserDto
import com.poisonedyouth.financemanagement.user.port.UserDto
import com.poisonedyouth.financemanagement.user.port.UserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

suspend fun mapFailureToHttpResponse(call: ApplicationCall, failure: Failure) {
    when (failure) {
        is Failure.ValidationFailure -> call.respond(HttpStatusCode.BadRequest, failure.message)
        is Failure.AlreadyExistFailure -> call.respond(HttpStatusCode.Conflict, failure.message)
        is Failure.NotFoundFailure -> call.respond(HttpStatusCode.NotFound, failure.message)
        else -> call.respond(HttpStatusCode.InternalServerError, failure.message)
    }
}

fun Application.configureUserRouting() {
    val userUseCase by inject<UserUseCase>()

    routing {
        route("/api/v1/user") {
            post {
                val userDto = call.receive<NewUserDto>()
                userUseCase.create(userDto).fold(
                    { failure -> mapFailureToHttpResponse(call, failure) }
                ) {
                    call.respond(
                        status = HttpStatusCode.Created,
                        message = mapOf(
                            "userId" to it.userId,
                            "password" to "password"
                        )
                    )
                }
            }
            authenticate("basic-auth") {
                put {
                    val userDto = call.receive<UserDto>()
                    userUseCase.update(userDto)
                        .fold(
                            { failure -> mapFailureToHttpResponse(call, failure) }
                        ) {
                            call.respond(
                                status = HttpStatusCode.OK,
                                message = "User with email '${userDto.email}' updated successfully."
                            )
                        }
                }
                delete {
                    val userId = call.parameters["userId"] ?: ""
                    userUseCase.delete(userId)
                        .fold(
                            { failure -> mapFailureToHttpResponse(call, failure) }
                        ) {
                            call.respond(
                                status = HttpStatusCode.OK,
                                message = "User with id '$userId' deleted successfully."
                            )
                        }
                }
                get {
                    val userId = call.parameters["userId"] ?: ""
                    userUseCase.findById(userId)
                        .fold(
                            { failure -> mapFailureToHttpResponse(call, failure) }
                        ) {
                            if (it == null) {
                                call.respond(
                                    status = HttpStatusCode.NotFound,
                                    message = "User with id '$userId' does not exist."
                                )
                                return@fold
                            }
                            call.respond(status = HttpStatusCode.OK, message = it)
                        }
                }
            }
        }
    }
}
