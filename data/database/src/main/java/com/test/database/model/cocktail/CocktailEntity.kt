package com.test.database.model.cocktail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.database.Table
import java.util.*

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
    val isFavorite: Boolean = false,

    @ColumnInfo(name = "date_added")
    val dateAdded: Date = Calendar.getInstance().time
)