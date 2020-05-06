package com.test.thecocktaildb.data

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface CocktailsRepository {

    fun searchCocktails(searchQuery: String): Single<Cocktails>

    fun saveCocktail(cocktail: Cocktail): Completable

    fun getCocktail(cocktailId: String): Maybe<Cocktail>

    fun getCocktails(): Maybe<List<Cocktail>>

    fun getNumberOfItems(): Observable<Long>
}