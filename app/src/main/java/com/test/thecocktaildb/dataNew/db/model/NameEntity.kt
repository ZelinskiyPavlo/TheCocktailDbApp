package com.test.thecocktaildb.dataNew.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.test.thecocktaildb.dataNew.db.Table

@Entity(tableName = Table.NAME,
    foreignKeys = [ForeignKey(
        entity = CocktailEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("cocktail_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
class NameEntity(
    @ColumnInfo(name = "cocktail_id")
    @PrimaryKey
    var cocktailId: Long = -1L,

    @ColumnInfo(name = "name_default")
    var nameDefault: String? = null,

    @ColumnInfo(name = "name_alternate")
    var nameAlternate: String? = null,

    @ColumnInfo(name = "name_es")
    var nameEs: String? = null,

    @ColumnInfo(name = "name_de")
    var nameDe: String? = null,

    @ColumnInfo(name = "name_fr")
    var nameFr: String? = null,

    @ColumnInfo(name = "name_zn_hans")
    var nameZhHans: String? = null,

    @ColumnInfo(name = "name_zn_hant")
    var nameZhHant: String? = null
)