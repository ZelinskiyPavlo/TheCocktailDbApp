package com.test.thecocktaildb.data.local

import com.test.thecocktaildb.data.Cocktail
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject

class CocktailsLocalDataSourceImpl @Inject constructor(private val cocktailsDao: CocktailsDao):
    CocktailsLocalDataSource {

    override fun getCocktail(cocktailId: String): Maybe<Cocktail> =
        cocktailsDao.getCocktail(cocktailId)

    override fun saveCocktail(cocktail: Cocktail): Completable =
        cocktailsDao.saveCocktail(cocktail)

    override fun getCocktails(): Maybe<List<Cocktail>> =
        cocktailsDao.getCocktails()

    override fun getNumberOfItems(): Observable<Long> =
        cocktailsDao.getNumberOfItems()

    override fun updateFavoriteState(cocktailId: String, state: Boolean): Completable =
        cocktailsDao.updateFavoriteState(cocktailId, state)
}