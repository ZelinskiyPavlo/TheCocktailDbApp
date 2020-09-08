package com.test.thecocktaildb.data.db.model.cocktail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.thecocktaildb.data.db.Table

@Entity(tableName = Table.MEASURE)
class MeasureEntity(
    @PrimaryKey
    @ColumnInfo(name = "measure")
    val measure: String = "",
)