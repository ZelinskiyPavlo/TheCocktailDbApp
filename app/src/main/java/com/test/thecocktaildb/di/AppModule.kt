package com.test.thecocktaildb.di

import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.data.CocktailsRepository
import com.test.thecocktaildb.data.local.CocktailsLocalDataSource
import com.test.thecocktaildb.data.remote.CocktailsRemoteDataSource
import com.test.thecocktaildb.utils.schedulers.AppSchedulerProvider
import com.test.thecocktaildb.utils.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DatabaseModule::class, RetrofitModule::class, ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideRepository(
        cocktailsRemoteDataSource: CocktailsRemoteDataSource,
        cocktailsLocalDataSource: CocktailsLocalDataSource,
        scheduler: AppSchedulerProvider
    ): CocktailsRepository =
        AppCocktailsRepository(cocktailsRemoteDataSource, cocktailsLocalDataSource, scheduler)


    @Singleton
    @Provides
    fun provideScheduler(): SchedulerProvider = AppSchedulerProvider()
}