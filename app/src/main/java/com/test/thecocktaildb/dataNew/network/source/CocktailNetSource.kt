package com.test.thecocktaildb.dataNew.network.source

import com.test.thecocktaildb.dataNew.network.model.cocktail.CocktailNetModel
import com.test.thecocktaildb.dataNew.network.source.base.BaseNetSource

interface CocktailNetSource: BaseNetSource {

    fun searchCocktails(searchQuery: String): List<CocktailNetModel>

    fun findCocktailById(id: String): CocktailNetModel
}