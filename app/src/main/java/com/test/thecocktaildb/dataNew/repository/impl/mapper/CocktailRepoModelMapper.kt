package com.test.thecocktaildb.dataNew.repository.impl.mapper

import com.test.thecocktaildb.dataNew.db.model.cocktail.CocktailDbModel
import com.test.thecocktaildb.dataNew.network.model.cocktail.CocktailNetModel
import com.test.thecocktaildb.dataNew.repository.impl.mapper.base.BaseRepoModelMapper
import com.test.thecocktaildb.dataNew.repository.model.CocktailRepoModel
import javax.inject.Inject

class CocktailRepoModelMapper @Inject constructor(
    private val localizedStringRepoModelMapper: LocalizedStringRepoModelMapper
) : BaseRepoModelMapper<CocktailRepoModel, CocktailDbModel, CocktailNetModel>() {

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
            isFavorite = isFavorite ?: false,
            dateAdded = dateAdded
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
            isFavorite = isFavorite,
            dateAdded = dateAdded
        )
    }

    override fun mapNetToRepo(net: CocktailNetModel): CocktailRepoModel  = with(net) {
        CocktailRepoModel(
            id = id,
            names = names.run(localizedStringRepoModelMapper::mapNetToRepo),
            category = category,
            alcoholType = alcoholType,
            glass = glass,
            image = image,
            instructions = instructions.run(localizedStringRepoModelMapper::mapNetToRepo),
            ingredients = ingredients,
            measures = measures
        )
    }

    override fun mapNetToDb(net: CocktailNetModel): CocktailDbModel = with(net) {
        CocktailDbModel(
            id = id,
            names = names.run(localizedStringRepoModelMapper::mapNetToDb),
            category = category,
            alcoholType = alcoholType,
            glass = glass,
            image = image,
            instructions = instructions.run(localizedStringRepoModelMapper::mapNetToDb),
            ingredients = ingredients,
            measures = measures,
            isFavorite = false,
        )
    }
}