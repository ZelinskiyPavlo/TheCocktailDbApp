package com.test.thecocktaildb.dataNew.db.impl.dao.base

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(t: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<T>)
}