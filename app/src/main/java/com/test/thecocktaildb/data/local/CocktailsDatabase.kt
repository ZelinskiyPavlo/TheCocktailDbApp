package com.test.thecocktaildb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.util.DateConverters

@Database(entities = [Cocktail::class], version = 1)
@TypeConverters(DateConverters::class)
abstract class CocktailsDatabase : RoomDatabase() {
    abstract fun cocktailsDao(): CocktailsDao
}