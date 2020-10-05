package com.test.impl.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.test.database.Table
import com.test.database.model.UserDbModel
import com.test.impl.dao.base.BaseDao

@Dao
interface UserDao : BaseDao<UserDbModel> {

    @get:Query("SELECT * FROM ${Table.USER}")
    val userLiveData: LiveData<UserDbModel?>

    @Query("SELECT * FROM ${Table.USER} LIMIT 1")
    fun getUser(): UserDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrReplaceUser(user: UserDbModel)

    @Transaction
    fun saveUser(user: UserDbModel) {
        deleteUser()
        addOrReplaceUser(user)
    }

    @Query("DELETE FROM ${Table.USER}")
    fun deleteUser()
}