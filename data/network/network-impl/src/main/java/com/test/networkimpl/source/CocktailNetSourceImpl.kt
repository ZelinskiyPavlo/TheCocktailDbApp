package com.test.networkimpl.source

import com.test.network.model.cocktail.CocktailNetModel
import com.test.network.source.CocktailNetSource
import com.test.networkimpl.service.CocktailApiService
import com.test.networkimpl.source.base.BaseNetSourceImpl
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