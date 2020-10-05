package com.test.network.source

import com.test.network.model.cocktail.CocktailNetModel
import com.test.network.source.base.BaseNetSource

interface CocktailNetSource: BaseNetSource {

    suspend fun searchCocktails(searchQuery: String): List<CocktailNetModel>

    suspend fun findCocktailById(id: String): CocktailNetModel?
}