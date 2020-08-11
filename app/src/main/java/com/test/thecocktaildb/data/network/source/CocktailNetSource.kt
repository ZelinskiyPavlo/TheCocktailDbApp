package com.test.thecocktaildb.data.network.source

import com.test.thecocktaildb.data.network.model.cocktail.CocktailNetModel
import com.test.thecocktaildb.data.network.source.base.BaseNetSource

interface CocktailNetSource: BaseNetSource {

    suspend fun searchCocktails(searchQuery: String): List<CocktailNetModel>

    suspend fun findCocktailById(id: String): CocktailNetModel?
}