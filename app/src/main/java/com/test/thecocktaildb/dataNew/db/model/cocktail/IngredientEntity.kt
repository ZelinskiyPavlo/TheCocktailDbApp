package com.test.thecocktaildb.dataNew.db.model.cocktail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.thecocktaildb.dataNew.db.Table

@Entity(tableName = Table.INGREDIENT)
class IngredientEntity(
    @PrimaryKey
    @ColumnInfo(name = "ingredient")
    val ingredient: String = "",
)