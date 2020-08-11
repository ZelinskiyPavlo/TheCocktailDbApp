package com.test.thecocktaildb.data.network.source

import com.test.thecocktaildb.data.network.source.base.BaseNetSource

interface AuthNetSource : BaseNetSource {

    /**
     * @return user token
     */
    suspend fun signIn(email: String, password: String): String

    /**
     * @return user token
     */
    suspend fun signUp(name: String, lastName: String, email: String, password: String): String
}