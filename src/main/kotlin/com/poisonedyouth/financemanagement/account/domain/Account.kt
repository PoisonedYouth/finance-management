package com.poisonedyouth.financemanagement.account.domain

import com.poisonedyouth.financemanagement.common.Identity

data class Account(
    val id: Identity,
    val name: String
)

data class NewAccount(
    val name: String
)
