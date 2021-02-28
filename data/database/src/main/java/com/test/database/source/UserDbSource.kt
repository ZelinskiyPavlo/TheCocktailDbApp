package com.test.database.source

import androidx.lifecycle.LiveData
import com.test.database.model.UserDbModel
import com.test.database.source.base.BaseDbSource

interface UserDbSource: BaseDbSource {

    val userLiveData: LiveData<UserDbModel?>

    suspend fun getUser(): UserDbModel?
    suspend fun saveUser(user: UserDbModel)
    suspend fun hasUser(): Boolean
    suspend fun deleteUser()
}