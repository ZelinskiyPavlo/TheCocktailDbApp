package com.test.thecocktaildb.data

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface CocktailsRepository {

    fun searchCocktails(searchQuery: String): Single<Cocktails>

    fun saveCocktails(cocktail: Cocktail): Completable

    fun getCocktails(cocktailId: String): Maybe<Cocktail>
}