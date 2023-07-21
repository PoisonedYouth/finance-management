package com.poisonedyouth.financemanagement.common

import com.poisonedyouth.financemanagement.failure.Failure
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import java.util.UUID

class IdentityTest : AnnotationSpec() {

    @Test
    fun `getIdOrNull returns null for NoIdentity`() {
        // given
        val identity = NoIdentity

        // when
        val actual = identity.getIdOrNull()

        // then
        actual shouldBe null
    }

    @Test
    fun `getIdOrNull returns id for UUIDIdentity`() {
        // given
        val uuid = UUID.randomUUID()
        val identity = UUIDIdentity(uuid)

        // when
        val actual = identity.getIdOrNull()

        // then
        actual shouldBe uuid
    }

    @Test
    fun `getIdOrFailure returns id for UUIDIdentity`() {
        // given
        val uuid = UUID.randomUUID()
        val identity = UUIDIdentity(uuid)

        // when
        val actual = identity.getIdOrFailure()

        // then
        actual shouldBeRight uuid
    }

    @Test
    fun `getIdOrFailure returns failure for NoIdentity`() {
        // given
        val identity = NoIdentity

        // when
        val actual = identity.getIdOrFailure()

        // then
        actual shouldBeLeft Failure.ValidationFailure("The id is not yet set.")
    }
}
