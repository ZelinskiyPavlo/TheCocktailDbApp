package com.test.database.model.cocktail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.database.Table

@Entity(tableName = Table.INGREDIENT)
class IngredientEntity(
    @PrimaryKey
    @ColumnInfo(name = "ingredient")
    val ingredient: String = "",
)