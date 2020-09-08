package com.test.thecocktaildb.data.remote

import com.test.thecocktaildb.data.Cocktails
import io.reactivex.Single
import javax.inject.Inject

class CocktailsRemoteDataSourceImpl @Inject constructor(
    private val cocktailsService: CocktailsService): CocktailsRemoteDataSource {

    override fun searchCocktails(searchQuery: String): Single<Cocktails> =
        cocktailsService.searchCocktails(searchQuery)

    override fun findCocktailById(id: String): Single<Cocktails> =
        cocktailsService.findCocktailsById(id)
}