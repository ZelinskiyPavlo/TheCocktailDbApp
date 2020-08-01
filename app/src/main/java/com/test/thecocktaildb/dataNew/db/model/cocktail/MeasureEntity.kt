package com.test.thecocktaildb.dataNew.db.model.cocktail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.thecocktaildb.dataNew.db.Table

@Entity(tableName = Table.MEASURE)
class MeasureEntity(
    @PrimaryKey
    @ColumnInfo(name = "measure")
    val measure: String = "",
)