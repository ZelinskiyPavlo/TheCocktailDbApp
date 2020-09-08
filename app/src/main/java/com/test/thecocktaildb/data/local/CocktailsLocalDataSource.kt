package com.test.thecocktaildb.data.local

import com.test.thecocktaildb.data.Cocktail
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

interface CocktailsLocalDataSource {

    fun getCocktail(cocktailId: String): Maybe<Cocktail>

    fun saveCocktail(cocktail: Cocktail): Completable

    fun getCocktails(): Maybe<List<Cocktail>>

    fun getNumberOfItems(): Observable<Long>

    fun updateFavoriteState(cocktailId: String, state: Boolean): Completable
}