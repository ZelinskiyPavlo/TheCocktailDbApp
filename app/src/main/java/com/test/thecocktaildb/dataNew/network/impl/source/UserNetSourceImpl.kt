package com.test.thecocktaildb.dataNew.network.impl.source

import com.test.thecocktaildb.dataNew.network.impl.service.UserApiService
import com.test.thecocktaildb.dataNew.network.impl.source.base.BaseNetSourceImpl
import com.test.thecocktaildb.dataNew.network.model.UserNetModel
import com.test.thecocktaildb.dataNew.network.source.UserNetSource
import java.io.File
import javax.inject.Inject

class UserNetSourceImpl @Inject constructor(apiService: UserApiService):
    BaseNetSourceImpl<UserApiService>(apiService),UserNetSource {

    override suspend fun getUser(): UserNetModel {
        return performRequest {
            getUser()
        }
    }

    override suspend fun updateUserLogo(avatar: File) {
    }

    override suspend fun updateUser(user: UserNetModel) {
        return performRequest {
            updateUser(user)
        }
    }
}