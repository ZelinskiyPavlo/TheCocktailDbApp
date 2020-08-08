package com.test.thecocktaildb.dataNew.network.source

import com.test.thecocktaildb.dataNew.network.source.base.BaseNetSource

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