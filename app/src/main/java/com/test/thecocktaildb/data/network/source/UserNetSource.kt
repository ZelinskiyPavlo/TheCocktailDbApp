package com.test.thecocktaildb.data.network.source

import com.test.thecocktaildb.data.network.model.UserNetModel
import com.test.thecocktaildb.data.network.source.base.BaseNetSource

interface UserNetSource: BaseNetSource {

    suspend fun getUser(): UserNetModel

    suspend fun updateUser(user: UserNetModel)
}