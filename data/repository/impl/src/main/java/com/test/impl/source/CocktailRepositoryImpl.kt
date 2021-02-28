package com.test.impl.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.test.database.source.CocktailDbSource
import com.test.impl.mapper.CocktailRepoModelMapper
import com.test.impl.source.base.BaseRepositoryImpl
import com.test.network.source.CocktailNetSource
import com.test.repository.model.CocktailRepoModel
import com.test.repository.source.CocktailRepository
import java.util.*
import javax.inject.Inject

class CocktailRepositoryImpl @Inject constructor(
    private val dbSource: CocktailDbSource,
    private val netSource: CocktailNetSource,
    private val mapper: CocktailRepoModelMapper
) : BaseRepositoryImpl(), CocktailRepository {

    override val cocktailListLiveData: LiveData<List<CocktailRepoModel>> =
        dbSource.cocktailListLiveData.map(mapper::mapDbToRepo)

    override suspend fun searchCocktails(query: String): List<CocktailRepoModel>? {
        return netSource.searchCocktails(query).run(mapper::mapNetToRepo)
    }

    override suspend fun hasCocktails() = dbSource.hasCocktails()

    override suspend fun getFirstCocktail(): CocktailRepoModel? {
        return dbSource.getFirstCocktail()?.run(mapper::mapDbToRepo)
    }

    override suspend fun getCocktailById(id: Long): CocktailRepoModel? {
        return dbSource.getCocktailById(id)?.run(mapper::mapDbToRepo).also { updateCocktailDate(id) }
            ?: findAndAddCocktailById(id)
    }

    override suspend fun findCocktailById(id: Long): CocktailRepoModel? {
        return netSource.findCocktailById(id.toString())?.run(mapper::mapNetToRepo)
    }

    override suspend fun findAndAddCocktailById(id: Long): CocktailRepoModel? {
        val cocktail = netSource.findCocktailById(id.toString())?.also {
            addOrReplaceCocktail(it.run(mapper::mapNetToRepo))
        }
        return cocktail?.run(mapper::mapNetToRepo)
    }

    override suspend fun getCocktails(): List<CocktailRepoModel>? =
        dbSource.getCocktails()?.map(mapper::mapDbToRepo)

    override suspend fun updateCocktailDate(cocktailId: Long) =
        dbSource.updateCocktailDate(Calendar.getInstance().time, cocktailId)

    override suspend fun updateCocktailFavoriteState(cocktailId: Long, isFavorite: Boolean) =
        dbSource.updateCocktailFavoriteState(cocktailId, isFavorite)

    override suspend fun addOrReplaceCocktail(cocktail: CocktailRepoModel) {
        dbSource.addOrReplaceCocktail(cocktail.run(mapper::mapRepoToDb))
    }

    override suspend fun addOrReplaceCocktails(vararg cocktail: CocktailRepoModel) {
        dbSource.addOrReplaceCocktails(
            *cocktail.map(mapper::mapRepoToDb).toTypedArray()
        )
    }

    override suspend fun replaceAllCocktails(vararg cocktail: CocktailRepoModel) {
        dbSource.replaceAllCocktails(
            *cocktail.map(mapper::mapRepoToDb).toTypedArray()
        )
    }

    override suspend fun deleteCocktails(vararg cocktail: CocktailRepoModel) {
        dbSource.deleteCocktails(
            *cocktail.map(mapper::mapRepoToDb).toTypedArray()
        )
    }

    override suspend fun deleteAllCocktails() {
        dbSource.deleteAllCocktails()
    }
}