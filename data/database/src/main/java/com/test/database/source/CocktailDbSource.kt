package com.test.database.source

import androidx.lifecycle.LiveData
import com.test.database.model.cocktail.CocktailDbModel
import com.test.database.source.base.BaseDbSource
import java.util.*

interface CocktailDbSource : BaseDbSource {

    val cocktailListLiveData: LiveData<List<CocktailDbModel>>

    suspend fun hasCocktails(): Boolean
    suspend fun getFirstCocktail(): CocktailDbModel?
    suspend fun getCocktailById(id: Long): CocktailDbModel?
    suspend fun getCocktails(): List<CocktailDbModel>?
    suspend fun updateCocktailDate(dateAdded: Date, cocktailId: Long)
    suspend fun updateCocktailFavoriteState(cocktailId: Long, isFavorite: Boolean)
    suspend fun addOrReplaceCocktail(cocktail: CocktailDbModel)
    suspend fun addOrReplaceCocktails(vararg cocktail: CocktailDbModel)
    suspend fun replaceAllCocktails(vararg cocktail: CocktailDbModel)
    suspend fun deleteCocktails(vararg cocktail: CocktailDbModel)
    suspend fun deleteAllCocktails()
}