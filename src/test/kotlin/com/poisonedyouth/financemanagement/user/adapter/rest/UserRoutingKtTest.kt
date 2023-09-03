package com.poisonedyouth.financemanagement.user.adapter.rest

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.poisonedyouth.financemanagement.notification.service.UserCredentialsService
import com.poisonedyouth.financemanagement.util.basicAuthHeader
import com.poisonedyouth.financemanagement.util.defaultUserId
import com.poisonedyouth.financemanagement.util.extractPassword
import com.poisonedyouth.financemanagement.util.extractUserId
import com.poisonedyouth.financemanagement.util.userIdRegex
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.http.parameters
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.testApplication
import org.slf4j.LoggerFactory
import java.util.UUID

class UserRoutingKtTest : AnnotationSpec() {

    private val logAppender = ListAppender<ILoggingEvent>()

    @BeforeEach
    fun addLogAppender() {
        val logger = LoggerFactory.getLogger(UserCredentialsService::class.java) as Logger
        logAppender.list.clear()
        logAppender.start()
        logger.addAppender(logAppender)
    }

    @AfterEach
    fun removeLogAppender() {
        val logger = LoggerFactory.getLogger(UserCredentialsService::class.java) as Logger
        logAppender.stop()
        logger.detachAppender(logAppender.name)
    }

    @Test
    fun `post request is adding new user`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        // given
        val body = """
                    {
                        "firstname": "John",
                        "lastname": "Doe",
                        "email": "john.doe@mail.com"
                    }
        """.trimIndent()

        // when
        val result = client.post("/api/v1/user") {
            setBody(body)
            headers {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }

        // then
        result.status shouldBe HttpStatusCode.Created
        result.bodyAsText() shouldContain "\"userId\" :"
    }

    @Test
    fun `post request is returning failure when email already exists`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        // given
        val body = """
                    {
                        "firstname": "John",
                        "lastname": "Doe",
                        "email": "john.doe@mail.com"
                    }
        """.trimIndent()

        client.post("/api/v1/user") {
            setBody(body)
            headers {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
        // when
        val result = client.post("/api/v1/user") {
            setBody(body)
            headers {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }

        // then
        result.status shouldBe HttpStatusCode.Conflict
        result.bodyAsText() shouldBeEqual "User with email 'john.doe@mail.com' already exists."
    }

    @Test
    fun `put request is returning failure when user does not exist`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        // given
        val body = """
                    {
                        "userId": "${UUID.randomUUID()}",
                        "firstname": "John",
                        "lastname": "Doe",
                        "email": "john.doe@mail.com"
                    }
        """.trimIndent()

        // when
        val result = client.put("/api/v1/user") {
            setBody(body)
            headers {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Authorization, basicAuthHeader("John", "password"))
            }
        }

        // then
        result.status shouldBe HttpStatusCode.Unauthorized
    }

    @Test
    fun `put request updates existing user`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        // given
        val body = """
                    {
                        "firstname": "Max",
                        "lastname": "Doe",
                        "email": "max.doe@mail.com"
                    }
        """.trimIndent()

        val createdUserResponse = client.post("/api/v1/user") {
            setBody(body)
            headers {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
        val userId = extractUserId(createdUserResponse)

        val logMessage = logAppender.list.first { it.formattedMessage.startsWith("New UserCredentials created") }

        // when
        val updatedBody = """
                    {
                        "userId": "$userId",
                        "firstname": "Max",
                        "lastname": "Doe",
                        "email": "max.doe@mail.com"
                    }
        """.trimIndent()
        val result = client.put("/api/v1/user") {
            setBody(updatedBody)
            headers {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Authorization, basicAuthHeader(userId, extractPassword(logMessage.formattedMessage)))
            }
        }

        // then
        result.status shouldBe HttpStatusCode.OK
        result.bodyAsText() shouldBeEqual "User with email 'max.doe@mail.com' updated successfully."
    }

    @Test
    fun `delete request is returning failure when user does not exist`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        // given
        val userId = defaultUserId.id

        // when
        val result = client.delete("/api/v1/user") {
            parameters {
                parameter("userId", userId)
            }
            headers {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Authorization, basicAuthHeader("John", "password"))
            }
        }

        // then
        result.status shouldBe HttpStatusCode.Unauthorized
    }

    @Test
    fun `delete request is deletes existing user`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        // given
        val body = """
                    {
                        "firstname": "John",
                        "lastname": "Doe",
                        "email": "john.doe@mail.com"
                    }
        """.trimIndent()

        // when
        val createdUserResponse = client.post("/api/v1/user") {
            setBody(body)
            headers {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
        val userId = extractUserId(createdUserResponse)

        val logMessage = logAppender.list.first { it.formattedMessage.startsWith("New UserCredentials created") }

        // when
        val result = client.delete("/api/v1/user") {
            headers {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Authorization, basicAuthHeader(userId, extractPassword(logMessage.formattedMessage)))
            }
        }

        // then
        result.status shouldBe HttpStatusCode.OK
        result.bodyAsText() shouldBeEqual "User with id '$userId' deleted successfully."
    }

    @Test
    fun `get request returns existing user`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        // given
        val body = """
                    {
                        "firstname": "Julia",
                        "lastname": "Doe",
                        "email": "julia.doe@mail.com"
                    }
        """.trimIndent()

        // when
        val createdUserReponse = client.post("/api/v1/user") {
            setBody(body)
            headers {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
        val userId = extractUserId(createdUserReponse)

        val logMessage = logAppender.list.first { it.formattedMessage.startsWith("New UserCredentials created") }

        // when
        val result = client.get("/api/v1/user") {
            headers {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Authorization, basicAuthHeader(userId, extractPassword(logMessage.formattedMessage)))
            }
        }

        // then
        result.status shouldBe HttpStatusCode.OK
        val resultBody = result.bodyAsText()
        resultBody shouldContain (userIdRegex)
        resultBody shouldContain " \"firstname\" : \"Julia\","
        resultBody shouldContain " \"lastname\" : \"Doe\","
        resultBody shouldContain " \"email\" : \"julia.doe@mail.com"
    }

    @Test
    fun `get request returns failure for not existing user`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        // given
        val userId = UUID.randomUUID()

        // when
        val result = client.get("/api/v1/user") {
            parameters {
                parameter("userId", userId)
            }
            headers {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Authorization, basicAuthHeader("John", "password"))
            }
        }

        // then
        result.status shouldBe HttpStatusCode.Unauthorized
    }
}
