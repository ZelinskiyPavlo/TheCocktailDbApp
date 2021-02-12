package com.test.presentation.mapper.cocktail

import com.test.presentation.mapper.base.BaseModelMapper
import com.test.presentation.model.cocktail.*
import com.test.repository.model.CocktailRepoModel
import javax.inject.Inject

class CocktailModelMapper @Inject constructor(
    private val localizedStringModelMapper: LocalizedStringModelMapper
) : BaseModelMapper<CocktailModel, CocktailRepoModel>() {

    override fun mapFrom(model: CocktailModel) = with(model) {
        CocktailRepoModel(
            id = id,
            names = names.run(localizedStringModelMapper::mapFrom),
            category = category.key,
            alcoholType = alcoholType.key,
            glass = glass.key,
            image = image,
            instructions = instructions.run(localizedStringModelMapper::mapFrom),
            ingredients = ingredients.map { it.key },
            measures = measures,
            isFavorite = isFavorite,
            dateAdded = dateAdded
        )
    }

    override fun mapTo(model: CocktailRepoModel)= with(model) {
        CocktailModel(
            id = id,
            names = names.run(localizedStringModelMapper::mapTo),
            category = CocktailCategory.values().firstOrNull { it.key == category } ?: CocktailCategory.UNDEFINED,
            alcoholType = CocktailAlcoholType.values().firstOrNull { it.key == alcoholType } ?: CocktailAlcoholType.UNDEFINED,
            glass = CocktailGlassType.values().firstOrNull { it.key == glass } ?: CocktailGlassType.UNDEFINED,
            image = image,
            instructions = instructions.run(localizedStringModelMapper::mapTo),
            ingredients = ingredients.map { ingredient ->  CocktailIngredient.values().firstOrNull { it.key == ingredient } ?: CocktailIngredient.UNDEFINED },
            measures = measures,
            isFavorite = isFavorite,
            dateAdded = dateAdded
        )
    }
}
