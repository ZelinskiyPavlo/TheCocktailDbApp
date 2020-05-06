package com.test.thecocktaildb.data

import com.test.thecocktaildb.data.local.CocktailsLocalDataSource
import com.test.thecocktaildb.data.remote.CocktailsRemoteDataSource
import com.test.thecocktaildb.utils.schedulers.AppSchedulerProvider
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class AppCocktailsRepository @Inject constructor(
    private val cocktailsRemoteDataSource: CocktailsRemoteDataSource,
    private val cocktailsLocalDataSource: CocktailsLocalDataSource,
    private val scheduler: AppSchedulerProvider
) : CocktailsRepository {

    override fun searchCocktails(searchQuery: String): Single<Cocktails> {
        return cocktailsRemoteDataSource.searchCocktails(searchQuery)
            .subscribeOn(scheduler.io()).observeOn(scheduler.ui())
    }

    override fun saveCocktail(cocktail: Cocktail): Completable {
        return cocktailsLocalDataSource.saveCocktail(cocktail)
            .subscribeOn(scheduler.io()).observeOn(scheduler.ui())
    }

    override fun getCocktail(cocktailId: String): Maybe<Cocktail> {
        return cocktailsLocalDataSource.getCocktail(cocktailId)
            .subscribeOn(scheduler.io()).observeOn(scheduler.ui())
    }

    override fun getCocktails(): Maybe<List<Cocktail>> {
        return cocktailsLocalDataSource.getCocktails()
            .subscribeOn(scheduler.io()).observeOn(scheduler.ui())
    }

    override fun getNumberOfItems(): Observable<Long> {
        return cocktailsLocalDataSource.getNumberOfItems()
            .subscribeOn(scheduler.io()).observeOn(scheduler.ui())
    }
}