package com.test.thecocktaildb.data

import com.test.thecocktaildb.data.remote.CocktailsRemoteDataSource
import com.test.thecocktaildb.utils.schedulers.AppSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class AppCocktailsRepository @Inject constructor(
    private val cocktailsRemoteDataSource: CocktailsRemoteDataSource,
    private val scheduler: AppSchedulerProvider
) : CocktailsRepository {

    override fun searchCocktails(searchQuery: String): Single<Cocktails> {
        return cocktailsRemoteDataSource.searchCocktails(searchQuery)
            .subscribeOn(scheduler.io()).observeOn(scheduler.ui())
    }
}