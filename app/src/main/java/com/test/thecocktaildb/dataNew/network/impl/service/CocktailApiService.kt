package com.test.thecocktaildb.dataNew.network.impl.service

import com.test.thecocktaildb.dataNew.network.model.cocktail.CocktailNetModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApiService {

    @GET("search.php")
    fun searchCocktails(@Query("s") searchQuery: String): Call<List<CocktailNetModel>>

    @GET("lookup.php")
    fun findCocktailsById(@Query("i") id: String): Call<List<CocktailNetModel>>
}