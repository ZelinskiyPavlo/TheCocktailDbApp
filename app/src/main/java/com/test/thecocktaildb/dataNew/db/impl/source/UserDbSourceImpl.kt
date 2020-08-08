package com.test.thecocktaildb.dataNew.db.impl.source

import com.test.thecocktaildb.dataNew.db.impl.dao.UserDao
import com.test.thecocktaildb.dataNew.db.model.UserDbModel
import com.test.thecocktaildb.dataNew.db.source.UserDbSource
import javax.inject.Inject

class UserDbSourceImpl @Inject constructor(private val userDao: UserDao) : UserDbSource {

    override val userLiveData = userDao.userLiveData

    override suspend fun getUser(): UserDbModel? = userDao.getUser()

    override suspend fun saveUser(user: UserDbModel) = userDao.saveUser(user)

    override suspend fun hasUser() = getUser() != null

    override suspend fun deleteUser() = userDao.deleteUser()
}