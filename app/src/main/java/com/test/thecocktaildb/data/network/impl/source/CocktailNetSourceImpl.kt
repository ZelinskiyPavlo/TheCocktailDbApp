package com.test.thecocktaildb.data.network.impl.source

import com.test.thecocktaildb.data.network.impl.service.CocktailApiService
import com.test.thecocktaildb.data.network.impl.source.base.BaseNetSourceImpl
import com.test.thecocktaildb.data.network.model.cocktail.CocktailNetModel
import com.test.thecocktaildb.data.network.source.CocktailNetSource
import javax.inject.Inject

class CocktailNetSourceImpl @Inject constructor(apiService: CocktailApiService):
    BaseNetSourceImpl<CocktailApiService>(apiService), CocktailNetSource {

    override suspend fun searchCocktails(searchQuery: String): List<CocktailNetModel> {
        return performRequest {
            searchCocktails(searchQuery).drinks ?: emptyList()
        }
    }

    override suspend fun findCocktailById(id: String): CocktailNetModel? {
        return performRequest {
            findCocktailById(id).drinks?.first()
        }
    }
}