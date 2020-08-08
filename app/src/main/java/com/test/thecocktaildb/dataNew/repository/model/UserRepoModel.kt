package com.test.thecocktaildb.dataNew.repository.model

data class UserRepoModel(
    val id: Long = 1L,
    val email: String = "",
    val name: String = "",
    val lastName: String = "",
    val avatar: String? = null
)