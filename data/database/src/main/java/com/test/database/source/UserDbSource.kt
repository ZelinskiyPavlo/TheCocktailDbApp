package com.test.database.source

import com.test.database.model.UserDbModel
import com.test.database.source.base.BaseDbSource
import kotlinx.coroutines.flow.Flow

interface UserDbSource: BaseDbSource {

    val userFlow: Flow<UserDbModel?>

    suspend fun getUser(): UserDbModel?
    suspend fun saveUser(user: UserDbModel)
    suspend fun hasUser(): Boolean
    suspend fun deleteUser()
}