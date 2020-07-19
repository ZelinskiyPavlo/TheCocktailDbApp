package com.test.thecocktaildb.dataNew.repository.source

import androidx.lifecycle.LiveData
import com.test.thecocktaildb.dataNew.repository.model.CocktailRepoModel
import com.test.thecocktaildb.dataNew.repository.source.base.BaseRepository

interface CocktailRepository : BaseRepository {

    val cocktailListLiveData: LiveData<List<CocktailRepoModel>>
    suspend fun hasCocktails(): Boolean
    suspend fun getFirstCocktail(): CocktailRepoModel?
    suspend fun getCocktailById(id: Long): CocktailRepoModel?
    suspend fun getCocktails(): List<CocktailRepoModel>?
    suspend fun addOrReplaceCocktail(cocktail: CocktailRepoModel)
    suspend fun addOrReplaceCocktails(vararg cocktail: CocktailRepoModel)
    suspend fun replaceAllCocktails(vararg cocktail: CocktailRepoModel)
    suspend fun deleteCocktails(vararg cocktail: CocktailRepoModel)
    suspend fun deleteAllCocktails()
}