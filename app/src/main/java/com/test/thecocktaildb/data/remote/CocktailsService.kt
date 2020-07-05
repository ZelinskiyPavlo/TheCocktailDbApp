package com.test.thecocktaildb.data.remote

import com.test.thecocktaildb.data.Cocktails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailsService {

    @GET("search.php")
    fun searchCocktails(@Query("s") searchQuery: String): Single<Cocktails>

    @GET("lookup.php")
    fun findCocktailsById(@Query("i") id: String): Single<Cocktails>

}