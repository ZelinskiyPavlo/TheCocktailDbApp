package com.test.databaseimpl.source

import com.test.database.model.cocktail.CocktailDbModel
import com.test.database.source.CocktailDbSource
import com.test.databaseimpl.dao.CocktailDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class CocktailDbSourceImpl @Inject constructor(
    private val cocktailDao: CocktailDao
) : CocktailDbSource {

    override val cocktailListFlow: Flow<List<CocktailDbModel>> =
        cocktailDao.cocktailListFlow.map { it.filter { cocktail -> cocktail.id != 0L } }

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