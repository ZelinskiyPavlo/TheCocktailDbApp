package com.test.database.model.cocktail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.database.Table

@Entity(tableName = Table.MEASURE)
class MeasureEntity(
    @PrimaryKey
    @ColumnInfo(name = "measure")
    val measure: String = "",
)