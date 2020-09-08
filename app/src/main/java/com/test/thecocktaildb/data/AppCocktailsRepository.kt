package com.test.thecocktaildb.data

import com.test.thecocktaildb.data.local.CocktailsLocalDataSource
import com.test.thecocktaildb.data.remote.CocktailsRemoteDataSource
import com.test.thecocktaildb.util.scheduler.AppSchedulerProvider
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class AppCocktailsRepository @Inject constructor(
    private val cocktailsRemoteDataSourceImpl: CocktailsRemoteDataSource,
    private val cocktailsLocalDataSourceImpl: CocktailsLocalDataSource,
    private val scheduler: AppSchedulerProvider
) : CocktailsRepository {

    override fun searchCocktails(searchQuery: String): Single<Cocktails> {
        return cocktailsRemoteDataSourceImpl.searchCocktails(searchQuery)
            .subscribeOn(scheduler.io()).observeOn(scheduler.ui())
    }

    override fun saveCocktail(cocktail: Cocktail): Completable {
        return cocktailsLocalDataSourceImpl.saveCocktail(cocktail)
            .subscribeOn(scheduler.io()).observeOn(scheduler.ui())
    }

    override fun getCocktail(cocktailId: String): Maybe<Cocktail> {
        return cocktailsLocalDataSourceImpl.getCocktail(cocktailId)
            .subscribeOn(scheduler.io()).observeOn(scheduler.ui())
    }

    override fun getCocktails(): Maybe<List<Cocktail>> {
        return cocktailsLocalDataSourceImpl.getCocktails()
            .subscribeOn(scheduler.io()).observeOn(scheduler.ui())
    }

    override fun getNumberOfItems(): Observable<Long> {
        return cocktailsLocalDataSourceImpl.getNumberOfItems()
            .subscribeOn(scheduler.io()).observeOn(scheduler.ui())
    }

    override fun updateFavoriteState(cocktailId: String, state: Boolean): Completable {
        return cocktailsLocalDataSourceImpl.updateFavoriteState(cocktailId, state)
            .subscribeOn(scheduler.io()).observeOn(scheduler.ui())
    }

    override fun findCocktailById(id: String): Single<Cocktails> {
        return cocktailsRemoteDataSourceImpl.findCocktailById(id)
            .subscribeOn(scheduler.io()).observeOn(scheduler.ui())
    }
}