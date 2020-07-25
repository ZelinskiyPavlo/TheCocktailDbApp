package com.test.thecocktaildb.dataNew.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.thecocktaildb.dataNew.db.Table

@Entity(tableName = Table.COCKTAIL)
data class CocktailEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = -1L,

    @ColumnInfo(name = "category")
    val category: String = "",

    @ColumnInfo(name = "alcohol_type")
    val alcoholType: String = "",

    @ColumnInfo(name = "glass")
    val glass: String = "",

    @ColumnInfo(name = "image")
    val image: String = "",

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
)