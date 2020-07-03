package com.test.thecocktaildb.data.remote

import com.test.thecocktaildb.data.Cocktails
import io.reactivex.Single
import javax.inject.Inject

class CocktailsRemoteDataSource @Inject constructor(private val cocktailsService: CocktailsService) {

    fun searchCocktails(searchQuery: String): Single<Cocktails> =
        cocktailsService.searchCocktails(searchQuery)

    fun findCocktailById(id: String): Single<Cocktails> =
        cocktailsService.findCocktailsById(id)
}