package com.poisonedyouth.financemanagement.account.domain

import com.poisonedyouth.financemanagement.common.UUIDIdentity

data class Account(
    val id: UUIDIdentity,
    val name: String
)

data class NewAccount(
    val name: String
)
