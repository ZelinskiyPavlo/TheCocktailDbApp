package com.test.thecocktaildb.data.db.model.cocktail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.test.thecocktaildb.data.db.Table

@Entity(tableName = Table.COCKTAIL_MEASURE,
    foreignKeys = [ForeignKey(
        entity = CocktailEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("cocktail_id"),
    ), ForeignKey(
        entity = MeasureEntity::class,
        parentColumns = arrayOf("measure"),
        childColumns = arrayOf("measure"),
    )], primaryKeys = ["cocktail_id", "measure"]
)
data class CocktailMeasureJunction(
    @ColumnInfo(name = "cocktail_id")
    var cocktailId: Long = -1L,

    @ColumnInfo(name = "measure")
    var measure: String = "",
)