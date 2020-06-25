package com.test.thecocktaildb.data.local

import com.test.thecocktaildb.data.Cocktail
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject

class CocktailsLocalDataSource @Inject constructor(private val cocktailsDao: CocktailsDao) {

    fun getCocktail(cocktailId: String): Maybe<Cocktail> =
        cocktailsDao.getCocktail(cocktailId)

    fun saveCocktail(cocktail: Cocktail): Completable =
        cocktailsDao.saveCocktail(cocktail)

    fun getCocktails(): Maybe<List<Cocktail>> =
        cocktailsDao.getCocktails()

    fun getNumberOfItems(): Observable<Long> =
        cocktailsDao.getNumberOfItems()

    fun updateFavoriteState(cocktailId: String, state: Boolean): Completable =
        cocktailsDao.updateFavoriteState(cocktailId, state)
}