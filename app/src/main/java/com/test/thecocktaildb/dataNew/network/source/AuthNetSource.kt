package com.test.thecocktaildb.dataNew.network.source

import com.test.thecocktaildb.dataNew.network.source.base.BaseNetSource

interface AuthNetSource: BaseNetSource {

    /**
     * @return login token
     */
    suspend fun signIn(email: String, password: String): String
}