package com.test.impl.source

import com.test.impl.service.UserApiService
import com.test.impl.source.base.BaseNetSourceImpl
import com.test.network.model.UserNetModel
import com.test.network.source.UserNetSource
import javax.inject.Inject

class UserNetSourceImpl @Inject constructor(apiService: UserApiService):
    BaseNetSourceImpl<UserApiService>(apiService), UserNetSource {

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