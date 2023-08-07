package com.poisonedyouth.financemanagement.util

import io.kotest.assertions.fail
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.util.encodeBase64

val userIdRegex = "userId\" : \"(\\w+-\\w+-\\w+-\\w+-\\w+)\"".toRegex()
val passwordRegex = "password=([0-9a-zA-Z]+)".toRegex()

suspend fun extractUserId(response: HttpResponse): String {
    val bodyText = response.bodyAsText()
    return userIdRegex.find(bodyText)?.groupValues?.get(1) ?: fail("UserId not found in response.")
}

fun extractPassword(logMessage: String): String {
    return passwordRegex.find(logMessage)?.groupValues?.get(1) ?: fail("Password not found in log.")
}

fun basicAuthHeader(name: String, password: String): String {
    val authString = "$name:$password"
    val authBuf = authString.toByteArray(Charsets.UTF_8).encodeBase64()

    return "Basic $authBuf"
}
