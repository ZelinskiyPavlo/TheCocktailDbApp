package com.test.thecocktaildb.di

import com.test.thecocktaildb.utils.schedulers.AppSchedulerProvider
import com.test.thecocktaildb.utils.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

//    TODO: provide repository, DAO, and other things

    @Singleton
    @Provides
    fun provideScheduler(): SchedulerProvider = AppSchedulerProvider()
}