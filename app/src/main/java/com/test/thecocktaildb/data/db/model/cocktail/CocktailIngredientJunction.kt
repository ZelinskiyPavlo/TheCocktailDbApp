package com.test.thecocktaildb.data.db.model.cocktail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.test.thecocktaildb.data.db.Table

@Entity(tableName = Table.COCKTAIL_INGREDIENT,
    foreignKeys = [ForeignKey(
        entity = CocktailEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("cocktail_id"),
    ), ForeignKey(
        entity = IngredientEntity::class,
        parentColumns = arrayOf("ingredient"),
        childColumns = arrayOf("ingredient"),
    )], primaryKeys = ["cocktail_id", "ingredient"]
)
data class CocktailIngredientJunction(
    @ColumnInfo(name = "cocktail_id")
    var cocktailId: Long = -1L,

    @ColumnInfo(name = "ingredient")
    var ingredient: String = "",
)