package com.poisonedyouth.financemanagement.e2e

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.poisonedyouth.financemanagement.notification.service.UserCredentialsService
import com.poisonedyouth.financemanagement.util.KtorServerTestExtension
import com.poisonedyouth.financemanagement.util.basicAuthHeader
import com.poisonedyouth.financemanagement.util.extractPassword
import com.poisonedyouth.financemanagement.util.extractUserId
import com.poisonedyouth.financemanagement.util.userIdRegex
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.testcontainers.junit.jupiter.Testcontainers

@ExtendWith(KtorServerTestExtension::class)
@Testcontainers
class UserE2ETest {
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
    fun `creation of new user and get user is working`() = runTest {
        // given
        val client = createHttpClient()
        val body = """
                    {
                        "firstname": "Hans",
                        "lastname": "Schmitt",
                        "email": "hans.schmitt@mail.com"
                    }
        """.trimIndent()

        // when
        val response = client.post("http://localhost:8080/api/v1/user") {
            setBody(body)
            headers {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }

        // then
        response.status shouldBe HttpStatusCode.Created

        // Get user
        val userId = extractUserId(response)

        val logMessage = logAppender.list.first { it.formattedMessage.startsWith("New UserCredentials created") }
        val password = extractPassword(logMessage.formattedMessage)

        val existingUserResponse = client.get("http://localhost:8080/api/v1/user") {
            headers {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Authorization, basicAuthHeader(userId, password))
            }
        }

        //
        existingUserResponse.status shouldBe HttpStatusCode.OK
        val resultBody = existingUserResponse.bodyAsText()
        resultBody shouldContain (userIdRegex)
        resultBody shouldContain " \"firstname\" : \"Hans\","
        resultBody shouldContain " \"lastname\" : \"Schmitt\","
        resultBody shouldContain " \"email\" : \"hans.schmitt@mail.com"
    }

    private fun createHttpClient(): HttpClient {
        val client = HttpClient(CIO) {
        }
        return client
    }
}
