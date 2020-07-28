package com.test.thecocktaildb.di.module.data

import androidx.room.Room
import com.test.thecocktaildb.CocktailApplication
import com.test.thecocktaildb.dataNew.db.Table
import com.test.thecocktaildb.dataNew.db.impl.CocktailAppRoomDatabase
import com.test.thecocktaildb.dataNew.db.impl.dao.CocktailDao
import com.test.thecocktaildb.dataNew.db.impl.dao.UserDao
import com.test.thecocktaildb.dataNew.db.impl.source.CocktailDbSourceImpl
import com.test.thecocktaildb.dataNew.db.impl.source.UserDbSourceImpl
import com.test.thecocktaildb.dataNew.db.source.CocktailDbSource
import com.test.thecocktaildb.dataNew.db.source.UserDbSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideCocktailsAppDatabase(app: CocktailApplication): CocktailAppRoomDatabase {
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