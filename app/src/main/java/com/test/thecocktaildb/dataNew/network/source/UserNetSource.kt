package com.test.thecocktaildb.dataNew.network.source

import com.test.thecocktaildb.dataNew.network.model.UserNetModel
import com.test.thecocktaildb.dataNew.network.source.base.BaseNetSource

interface UserNetSource: BaseNetSource {

    suspend fun getUser(): UserNetModel

    suspend fun updateUser(user: UserNetModel)
}