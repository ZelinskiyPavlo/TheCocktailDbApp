package com.test.databaseimpl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.test.database.Table
import com.test.database.model.UserDbModel
import com.test.databaseimpl.dao.base.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<UserDbModel> {

    @get:Query("SELECT * FROM ${Table.USER}")
    val userFlow: Flow<UserDbModel?>

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