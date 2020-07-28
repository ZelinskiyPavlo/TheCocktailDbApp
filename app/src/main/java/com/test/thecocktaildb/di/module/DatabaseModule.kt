package com.test.thecocktaildb.di.module

import androidx.room.Room
import com.test.thecocktaildb.CocktailApplication
import com.test.thecocktaildb.data.local.CocktailsDao
import com.test.thecocktaildb.data.local.CocktailsDatabase
import com.test.thecocktaildb.data.local.CocktailsLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

// TODO: 27.07.2020 Old module
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideCocktailsDatabase(app: CocktailApplication): CocktailsDatabase {
        return Room.databaseBuilder(app, CocktailsDatabase::class.java, "CocktailsDatabase")
            .build()
    }

    @Singleton
    @Provides
    fun provideCocktailsDao(database: CocktailsDatabase): CocktailsDao = database.cocktailsDao()

    @Singleton
    @Provides
    fun provideLocalDataSource(cocktailsDao: CocktailsDao): CocktailsLocalDataSourceImpl =
        CocktailsLocalDataSourceImpl(cocktailsDao)
}