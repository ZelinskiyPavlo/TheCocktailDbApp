package com.test.thecocktaildb.dataNew.repository.impl.mapper

import com.test.thecocktaildb.dataNew.db.model.CocktailDbModel
import com.test.thecocktaildb.dataNew.repository.impl.mapper.base.BaseRepoModelMapper
import com.test.thecocktaildb.dataNew.repository.model.CocktailRepoModel
import javax.inject.Inject

class CocktailRepoModelMapper @Inject constructor(
    private val localizedStringRepoModelMapper: LocalizedStringRepoModelMapper
) : BaseRepoModelMapper<CocktailRepoModel, CocktailDbModel, Any /*CocktailNetModel*/>() {

    override fun mapDbToRepo(db: CocktailDbModel): CocktailRepoModel = with(db) {
        CocktailRepoModel(
            id = id,
            names = names.run(localizedStringRepoModelMapper::mapDbToRepo),
            category = category ?: "",
            alcoholType = alcoholType ?: "",
            glass = glass ?: "",
            image = image ?: "",
            instructions = instructions.run(localizedStringRepoModelMapper::mapDbToRepo),
            ingredients = ingredients,
            measures = measures,
            isFavorite = isFavorite ?: false
        )
    }

    override fun mapRepoToDb(repo: CocktailRepoModel): CocktailDbModel = with(repo) {
        CocktailDbModel(
            id = id,
            names = names.run(localizedStringRepoModelMapper::mapRepoToDb),
            category = category,
            alcoholType = alcoholType,
            glass = glass,
            image = image,
            instructions = instructions.run(localizedStringRepoModelMapper::mapRepoToDb),
            ingredients = ingredients,
            measures = measures,
            isFavorite = isFavorite
        )
    }
}