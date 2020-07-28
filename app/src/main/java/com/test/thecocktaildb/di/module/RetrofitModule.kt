package com.test.thecocktaildb.di.module

import com.test.thecocktaildb.data.remote.CocktailsService
import com.test.thecocktaildb.dataNew.network.NetConstant
import com.test.thecocktaildb.di.DiConstant
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

// TODO: 27.07.2020 old module
@Module
class RetrofitModule {

    @Singleton
    @Provides
    @Named(DiConstant.OLD_RETROFIT)
    fun provideOldRetrofit(): Retrofit = Retrofit.Builder().baseUrl(NetConstant.Base_Url.COCKTAIL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()

    @Singleton
    @Provides
    fun provideCocktailsService(@Named(DiConstant.OLD_RETROFIT) retrofit: Retrofit): CocktailsService =
        retrofit.create(CocktailsService::class.java)
}