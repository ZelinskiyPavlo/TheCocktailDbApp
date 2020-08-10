package com.test.thecocktaildb.dataNew.db.impl

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.thecocktaildb.dataNew.db.impl.dao.CocktailDao
import com.test.thecocktaildb.dataNew.db.impl.typeconverter.StringListToStringConverter
import com.test.thecocktaildb.dataNew.db.model.*
import com.test.thecocktaildb.util.DateConverter

@Database(
    version = 1,
    entities = [CocktailEntity::class, NameEntity::class, InstructionEntity::class,
        IngredientEntity::class, CocktailIngredientJunction::class,
        CocktailMeasureJunction::class, MeasureEntity::class],
    views = [CocktailDbModel::class],
    exportSchema = false
)
@TypeConverters(DateConverter::class, StringListToStringConverter::class)
abstract class CocktailAppRoomDatabase : RoomDatabase() {

    abstract fun cocktailDao(): CocktailDao
}