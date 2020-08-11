package com.test.thecocktaildb.data.network.impl.source

import com.test.thecocktaildb.data.network.impl.service.UserApiService
import com.test.thecocktaildb.data.network.impl.source.base.BaseNetSourceImpl
import com.test.thecocktaildb.data.network.model.UserNetModel
import com.test.thecocktaildb.data.network.source.UserNetSource
import javax.inject.Inject

class UserNetSourceImpl @Inject constructor(apiService: UserApiService):
    BaseNetSourceImpl<UserApiService>(apiService),UserNetSource {

    override suspend fun getUser(): UserNetModel {
        return performRequest {
            getUser()
        }
    }

    override suspend fun updateUser(user: UserNetModel) {
        return performRequest {
            updateUser(user)
        }
    }
}