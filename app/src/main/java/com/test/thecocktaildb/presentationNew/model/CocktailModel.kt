package com.test.thecocktaildb.presentationNew.model

import java.util.*

data class CocktailModel(
    val id: Long = -1L,
    val names: LocalizedStringModel = LocalizedStringModel(),
    val category: CocktailCategory = CocktailCategory.UNDEFINED,
    val alcoholType: CocktailAlcoholType = CocktailAlcoholType.UNDEFINED,
    val glass: CocktailGlass = CocktailGlass.UNDEFINED,
    val image: String = "",
    val instructions: LocalizedStringModel = LocalizedStringModel(),
    val ingredients: List<CocktailIngredient> = emptyList(),
    val measures: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val dateAdded: Date = Calendar.getInstance().time
)