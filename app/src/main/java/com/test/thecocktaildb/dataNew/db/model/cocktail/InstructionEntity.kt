package com.test.thecocktaildb.dataNew.db.model.cocktail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.test.thecocktaildb.dataNew.db.Table

@Entity(tableName = Table.INSTRUCTION,
    foreignKeys = [ForeignKey(
        entity = CocktailEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("cocktail_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
class InstructionEntity(
    @ColumnInfo(name = "cocktail_id")
    @PrimaryKey
    var cocktailId: Long = -1L,

    @ColumnInfo(name = "instruction_default")
    var instructionDefault: String? = null,

    @ColumnInfo(name = "instruction_alternate")
    var instructionAlternate: String? = null,

    @ColumnInfo(name = "instruction_es")
    var instructionEs: String? = null,

    @ColumnInfo(name = "instruction_de")
    var instructionDe: String? = null,

    @ColumnInfo(name = "instruction_fr")
    var instructionFr: String? = null,

    @ColumnInfo(name = "instruction_zn_hans")
    var instructionZhHans: String? = null,

    @ColumnInfo(name = "instruction_zn_hant")
    var instructionZhHant: String? = null
)