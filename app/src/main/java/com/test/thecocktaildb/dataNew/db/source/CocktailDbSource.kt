package com.test.thecocktaildb.dataNew.db.source

import androidx.lifecycle.LiveData
import com.test.thecocktaildb.dataNew.db.model.CocktailDbModel

interface CocktailDbSource: BaseDbSource {

    val cocktailListLiveData: LiveData<List<CocktailDbModel>>

    suspend fun hasCocktails(): Boolean
    suspend fun getFirstCocktail(): CocktailDbModel?
    suspend fun getCocktailById(id: Long): CocktailDbModel?
    suspend fun addOrReplaceCocktail(cocktail: CocktailDbModel)
    suspend fun addOrReplaceCocktails(vararg cocktail: CocktailDbModel)
    suspend fun replaceAllCocktails(vararg cocktail: CocktailDbModel)
    suspend fun deleteCocktails(vararg cocktail: CocktailDbModel)
    suspend fun deleteAllCocktails()
}