package com.test.thecocktaildb.dataNew.db.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.thecocktaildb.dataNew.db.Table

@Entity(tableName = Table.COCKTAIL)
data class CocktailDbModel @JvmOverloads constructor(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = -1L,

    @Embedded(prefix = "name_")
    val names: LocalizedStringDbModel = LocalizedStringDbModel(),

    @ColumnInfo(name = "category")
    val category: String = "",

    @ColumnInfo(name = "alcohol_type")
    val alcoholType: String = "",

    @ColumnInfo(name = "glass")
    val glass: String = "",

    @ColumnInfo(name = "image")
    val image: String = "",

    @Embedded(prefix = "instruction_")
    val instructions: LocalizedStringDbModel = LocalizedStringDbModel(),

    @ColumnInfo(name = "ingredients")
    val ingredients: List<String> = emptyList(),

    @ColumnInfo(name = "measures")
    val measures: List<String> = emptyList(),

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
)