package com.test.thecocktaildb.presentationNew.mapper

import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.presentationNew.mapper.base.BaseModelMapper
import com.test.thecocktaildb.presentationNew.model.*
import com.test.thecocktaildb.ui.cocktailDetailsScreen.Ingredient
import javax.inject.Inject

class CocktailMapper @Inject constructor() : BaseModelMapper<Cocktail, CocktailModel>() {

    override fun mapFrom(model: Cocktail) = with(model) {
        CocktailModel(
            id = idDrink.toLong(),
            names = LocalizedStringModel(strDrink),
            category = CocktailCategory.values().firstOrNull { it.key == strCategory }
                ?: CocktailCategory.UNDEFINED,
            alcoholType = CocktailAlcoholType.values().firstOrNull { it.key == strAlcoholic }
                ?: CocktailAlcoholType.UNDEFINED,
            glass = CocktailGlass.values().firstOrNull { it.key == strGlass }
                ?: CocktailGlass.UNDEFINED,
            image = strDrinkThumb ?: "",
            instructions = LocalizedStringModel(strInstructions ?: ""),
            ingredients = createIngredientsList()
                .filter { ingredient -> ingredient.name != null }
                .map { ingredient: Ingredient ->
                    CocktailIngredient.values().firstOrNull { it.key == ingredient.name }
                        ?: CocktailIngredient.UNDEFINED
                },
            measures = createIngredientsList().filter { it.measure != null }.map {
                if (it.measure != null) it.measure ?: ""
                else ""
            },
            isFavorite = isFavorite
        )
    }
}