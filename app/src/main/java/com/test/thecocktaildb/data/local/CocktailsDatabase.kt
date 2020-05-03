package com.test.thecocktaildb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.thecocktaildb.data.Cocktail

@Database(entities = [Cocktail::class], version = 1)
abstract class CocktailsDatabase : RoomDatabase() {
    abstract fun cocktailsDao(): CocktailsDao
}