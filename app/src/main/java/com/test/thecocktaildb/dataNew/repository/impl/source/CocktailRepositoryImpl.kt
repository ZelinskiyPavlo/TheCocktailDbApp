package com.test.thecocktaildb.dataNew.repository.impl.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.test.thecocktaildb.dataNew.db.source.CocktailDbSource
import com.test.thecocktaildb.dataNew.repository.impl.mapper.CocktailRepoModelMapper
import com.test.thecocktaildb.dataNew.repository.impl.source.base.BaseRepositoryImpl
import com.test.thecocktaildb.dataNew.repository.model.CocktailRepoModel
import com.test.thecocktaildb.dataNew.repository.source.CocktailRepository
import javax.inject.Inject

class CocktailRepositoryImpl @Inject constructor(
    private val dbSource: CocktailDbSource,
    private val mapper: CocktailRepoModelMapper
) : BaseRepositoryImpl(), CocktailRepository {

    override val cocktailListLiveData: LiveData<List<CocktailRepoModel>> =
        dbSource.cocktailListLiveData.map(mapper::mapDbToRepo)

    override suspend fun hasCocktails() = dbSource.hasCocktails()

    override suspend fun getFirstCocktail(): CocktailRepoModel? {
        return dbSource.getFirstCocktail()?.run(mapper::mapDbToRepo)
    }

    override suspend fun getCocktailById(id: Long): CocktailRepoModel? {
        return dbSource.getCocktailById(id)?.run(mapper::mapDbToRepo)
    }

    override suspend fun getCocktails(): List<CocktailRepoModel>? =
        dbSource.getCocktails()?.map(mapper::mapDbToRepo)

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