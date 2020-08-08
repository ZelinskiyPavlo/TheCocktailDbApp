package com.test.thecocktaildb.dataNew.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.thecocktaildb.dataNew.db.Table

@Entity(tableName = Table.USER)
data class UserDbModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = 1L,

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "last_name")
    val lastName: String = "",

    @ColumnInfo(name = "avatar")
    val avatar: String? = null
)