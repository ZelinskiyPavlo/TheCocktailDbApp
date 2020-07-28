package com.test.thecocktaildb.dataNew.repository.source

import com.test.thecocktaildb.dataNew.repository.source.base.BaseRepository

interface AuthRepository : BaseRepository {

    /**
     * @return true - if user has already its profile data filled (go to Main)
     * Otherwise user must fill profile data
     */
    suspend fun signIn(email: String, password: String): Boolean
}