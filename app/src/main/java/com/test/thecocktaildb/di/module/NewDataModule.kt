package com.test.thecocktaildb.di.module

import android.content.Context
import androidx.room.Room
import com.test.thecocktaildb.CocktailApplication
import com.test.thecocktaildb.dataNew.db.Table
import com.test.thecocktaildb.dataNew.db.impl.CocktailAppRoomDatabase
import com.test.thecocktaildb.dataNew.db.impl.SharedPrefsHelper
import com.test.thecocktaildb.dataNew.db.impl.dao.CocktailDao
import com.test.thecocktaildb.dataNew.db.impl.source.AppSetingLocalSourceImpl
import com.test.thecocktaildb.dataNew.db.impl.source.CocktailDbSourceImpl
import com.test.thecocktaildb.dataNew.db.source.AppSettingLocalSource
import com.test.thecocktaildb.dataNew.db.source.CocktailDbSource
import com.test.thecocktaildb.dataNew.repository.impl.mapper.CocktailRepoModelMapper
import com.test.thecocktaildb.dataNew.repository.impl.source.AppSettingRepositoryImpl
import com.test.thecocktaildb.dataNew.repository.impl.source.CocktailRepositoryImpl
import com.test.thecocktaildb.dataNew.repository.source.AppSettingRepository
import com.test.thecocktaildb.dataNew.repository.source.CocktailRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NewDataModule {

    @Singleton
    @Provides
    fun provideCocktailsAppDatabase(app: CocktailApplication): CocktailAppRoomDatabase {
        return Room.databaseBuilder(app, CocktailAppRoomDatabase::class.java, Table.COCKTAIL)
            .build()
    }

    @Singleton
    @Provides
    fun provideCocktailDao(database: CocktailAppRoomDatabase): CocktailDao = database.cocktailDao()

    @Singleton
    @Provides
    fun provideLocalDbSourceImpl(cocktailDao: CocktailDao): CocktailDbSource =
        CocktailDbSourceImpl(cocktailDao)

    @Singleton
    @Provides
    fun provideRepositoryImpl(
        cocktailDbSourceImpl: CocktailDbSourceImpl,
        mapper: CocktailRepoModelMapper
    ): CocktailRepository =
        CocktailRepositoryImpl(cocktailDbSourceImpl, mapper)

    @Singleton
    @Provides
    fun provideAppSettingLocalSource(sharedPrefsHelper: SharedPrefsHelper): AppSettingLocalSource =
        AppSetingLocalSourceImpl(sharedPrefsHelper)

    @Singleton
    @Provides
    fun provideSharedPrefsHelper(app: CocktailApplication): SharedPrefsHelper =
        SharedPrefsHelper(app.applicationContext.getSharedPreferences("TEST", Context.MODE_PRIVATE))

    @Singleton
    @Provides
    fun provideAppSettingRepository(appSettingLocalSource: AppSettingLocalSource):
            AppSettingRepository = AppSettingRepositoryImpl(appSettingLocalSource)
}