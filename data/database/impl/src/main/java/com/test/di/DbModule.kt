package com.test.di

import android.app.Application
import androidx.room.Room
import com.test.database.Table
import com.test.database.source.CocktailDbSource
import com.test.database.source.UserDbSource
import com.test.impl.CocktailAppRoomDatabase
import com.test.impl.dao.CocktailDao
import com.test.impl.dao.UserDao
import com.test.impl.source.CocktailDbSourceImpl
import com.test.impl.source.UserDbSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideCocktailsAppDatabase(app: Application): CocktailAppRoomDatabase {
        return Room.databaseBuilder(app, CocktailAppRoomDatabase::class.java, Table.COCKTAIL_DB_NAME)
            .addMigrations(CocktailAppRoomDatabase.MIGRATION_1_2)
            .build()
    }

    @Singleton
    @Provides
    fun provideCocktailDao(database: CocktailAppRoomDatabase): CocktailDao = database.cocktailDao()

    @Singleton
    @Provides
    fun provideCocktailDbSource(cocktailDao: CocktailDao): CocktailDbSource =
        CocktailDbSourceImpl(cocktailDao)

    @Singleton
    @Provides
    fun provideUserDao(database: CocktailAppRoomDatabase): UserDao = database.userDao()

    @Singleton
    @Provides
    fun provideUserDbSource(userDao: UserDao): UserDbSource =
        UserDbSourceImpl(userDao)
}