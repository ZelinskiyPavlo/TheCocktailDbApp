package com.test.networkimpl.source

import com.test.network.model.UserNetModel
import com.test.network.source.UserNetSource
import com.test.networkimpl.service.UserApiService
import com.test.networkimpl.source.base.BaseNetSourceImpl
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