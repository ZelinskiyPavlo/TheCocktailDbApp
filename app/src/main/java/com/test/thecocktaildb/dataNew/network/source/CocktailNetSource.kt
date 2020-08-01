package com.test.thecocktaildb.dataNew.network.source

import com.test.thecocktaildb.dataNew.network.model.cocktail.CocktailNetModel
import com.test.thecocktaildb.dataNew.network.source.base.BaseNetSource

interface CocktailNetSource: BaseNetSource {

    suspend fun searchCocktails(searchQuery: String): List<CocktailNetModel>

    suspend fun findCocktailById(id: String): CocktailNetModel?
}