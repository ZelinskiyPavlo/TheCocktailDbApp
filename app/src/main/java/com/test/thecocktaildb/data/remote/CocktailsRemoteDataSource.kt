package com.test.thecocktaildb.data.remote

import com.test.thecocktaildb.data.Cocktails
import io.reactivex.Single

interface CocktailsRemoteDataSource {

    fun searchCocktails(searchQuery: String): Single<Cocktails>

    fun findCocktailById(id: String): Single<Cocktails>
}