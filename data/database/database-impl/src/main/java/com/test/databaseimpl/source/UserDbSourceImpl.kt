package com.test.databaseimpl.source

import com.test.database.model.UserDbModel
import com.test.database.source.UserDbSource
import com.test.databaseimpl.dao.UserDao
import javax.inject.Inject

class UserDbSourceImpl @Inject constructor(private val userDao: UserDao) : UserDbSource {

    override val userFlow = userDao.userFlow

    override suspend fun getUser(): UserDbModel? = userDao.getUser()

    override suspend fun saveUser(user: UserDbModel) = userDao.saveUser(user)

    override suspend fun hasUser() = getUser() != null

    override suspend fun deleteUser() = userDao.deleteUser()
}