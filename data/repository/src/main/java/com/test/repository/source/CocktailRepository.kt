package com.test.repository.source

import com.test.repository.model.CocktailRepoModel
import com.test.repository.source.base.BaseRepository
import kotlinx.coroutines.flow.Flow

interface CocktailRepository : BaseRepository {

    val cocktailListFlow: Flow<List<CocktailRepoModel>>
    suspend fun searchCocktails(query: String): List<CocktailRepoModel>?
    suspend fun hasCocktails(): Boolean
    suspend fun getFirstCocktail(): CocktailRepoModel?
    suspend fun getCocktailById(id: Long): CocktailRepoModel?
    suspend fun findCocktailById(id: Long): CocktailRepoModel?
    suspend fun findAndAddCocktailById(id: Long): CocktailRepoModel?
    suspend fun getCocktails(): List<CocktailRepoModel>?
    suspend fun updateCocktailDate(cocktailId: Long)
    suspend fun updateCocktailFavoriteState(cocktailId: Long, isFavorite: Boolean)
    suspend fun addOrReplaceCocktail(cocktail: CocktailRepoModel)
    suspend fun addOrReplaceCocktails(vararg cocktail: CocktailRepoModel)
    suspend fun replaceAllCocktails(vararg cocktail: CocktailRepoModel)
    suspend fun deleteCocktails(vararg cocktail: CocktailRepoModel)
    suspend fun deleteAllCocktails()
}