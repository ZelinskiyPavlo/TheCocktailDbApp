package com.test.thecocktaildb.data.network.impl.service

import com.test.thecocktaildb.data.network.model.cocktail.CocktailListResponseNetModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApiService {

    @GET("search.php")
    suspend fun searchCocktails(@Query("s") searchQuery: String): CocktailListResponseNetModel

    @GET("lookup.php")
    suspend fun findCocktailById(@Query("i") id: String): CocktailListResponseNetModel
}