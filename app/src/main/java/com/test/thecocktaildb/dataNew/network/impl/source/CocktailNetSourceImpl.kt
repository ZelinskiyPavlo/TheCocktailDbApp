package com.test.thecocktaildb.dataNew.network.impl.source

import com.test.thecocktaildb.dataNew.network.impl.service.CocktailApiService
import com.test.thecocktaildb.dataNew.network.impl.source.base.BaseNetSourceImpl
import com.test.thecocktaildb.dataNew.network.model.cocktail.CocktailNetModel
import com.test.thecocktaildb.dataNew.network.source.CocktailNetSource
import javax.inject.Inject

class CocktailNetSourceImpl @Inject constructor(apiService: CocktailApiService):
    BaseNetSourceImpl<CocktailApiService>(apiService), CocktailNetSource {

    override fun searchCocktails(searchQuery: String): List<CocktailNetModel> {
        TODO()
    }

    override fun findCocktailById(id: String): CocktailNetModel {
        TODO("Not yet implemented")
    }
}