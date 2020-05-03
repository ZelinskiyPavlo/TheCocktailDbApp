package com.test.thecocktaildb.data

import io.reactivex.Single

interface CocktailsRepository {

    fun searchCocktails(searchQuery: String): Single<Cocktails>
}