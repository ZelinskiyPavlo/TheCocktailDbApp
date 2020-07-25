package com.test.thecocktaildb.dataNew.db.impl.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.test.thecocktaildb.dataNew.db.impl.dao.CocktailDao
import com.test.thecocktaildb.dataNew.db.model.CocktailDbModel
import com.test.thecocktaildb.dataNew.db.source.CocktailDbSource
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