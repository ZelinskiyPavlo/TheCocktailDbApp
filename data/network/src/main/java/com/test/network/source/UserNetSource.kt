package com.test.network.source

import com.test.network.model.UserNetModel
import com.test.network.source.base.BaseNetSource

interface UserNetSource: BaseNetSource {

    suspend fun getUser(): UserNetModel

    suspend fun updateUser(user: UserNetModel)
}