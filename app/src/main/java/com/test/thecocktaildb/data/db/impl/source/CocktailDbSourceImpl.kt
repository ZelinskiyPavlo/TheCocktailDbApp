package com.test.thecocktaildb.data.db.impl.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.test.thecocktaildb.data.db.impl.dao.CocktailDao
import com.test.thecocktaildb.data.db.model.cocktail.CocktailDbModel
import com.test.thecocktaildb.data.db.source.CocktailDbSource
import java.util.*
import javax.inject.Inject

class CocktailDbSourceImpl @Inject constructor(
    private val cocktailDao: CocktailDao
) : CocktailDbSource {

    override val cocktailListLiveData: LiveData<List<CocktailDbModel>> =
        cocktailDao.cocktailListLiveData.map { it.filter { cocktail -> cocktail.id != 0L } }

    override suspend fun hasCocktails() = cocktailDao.getFirstCocktail() != null

    override suspend fun getFirstCocktail() = cocktailDao.getFirstCocktail()

    override suspend fun getCocktailById(id: Long) = cocktailDao.getCocktailById(id)

    override suspend fun getCocktails(): List<CocktailDbModel>? =
        cocktailDao.getCocktails()?.filter { cocktail -> cocktail.id != 0L }

    override suspend fun updateCocktailDate(dateAdded: Date, cocktailId: Long) =
        cocktailDao.updateCocktailDateAdded(dateAdded, cocktailId)

    override suspend fun updateCocktailFavoriteState(cocktailId: Long, isFavorite: Boolean) =
        cocktailDao.updateCocktailFavoriteState(cocktailId, isFavorite)

    override suspend fun addOrReplaceCocktail(cocktail: CocktailDbModel) {
        cocktailDao.addOrReplaceCocktail(cocktail)
    }

    override suspend fun addOrReplaceCocktails(vararg cocktail: CocktailDbModel) {
        cocktailDao.addOrReplaceCocktails(*cocktail)
    }

    override suspend fun replaceAllCocktails(vararg cocktail: CocktailDbModel) {
        cocktailDao.replaceAllCocktails(*cocktail)
    }

    override suspend fun deleteCocktails(vararg cocktail: CocktailDbModel) {
        cocktailDao.deleteCocktails(*cocktail)
    }

    override suspend fun deleteAllCocktails() {
        cocktailDao.deleteAllCocktails()
    }
}