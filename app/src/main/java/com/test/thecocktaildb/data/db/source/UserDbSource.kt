package com.test.thecocktaildb.data.db.source

import androidx.lifecycle.LiveData
import com.test.thecocktaildb.data.db.model.UserDbModel
import com.test.thecocktaildb.data.db.source.base.BaseDbSource

interface UserDbSource: BaseDbSource {

    val userLiveData: LiveData<UserDbModel?>

    suspend fun getUser(): UserDbModel?
    suspend fun saveUser(user: UserDbModel)
    suspend fun hasUser(): Boolean
    suspend fun deleteUser()
}