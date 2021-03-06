package com.test.presentation.model.cocktail

import com.test.presentation.model.cocktail.type.CocktailAlcoholType
import com.test.presentation.model.cocktail.type.CocktailCategory
import com.test.presentation.model.cocktail.type.CocktailGlassType
import com.test.presentation.model.cocktail.type.CocktailIngredient
import java.util.*

data class CocktailModel(
    val id: Long = -1L,
    val names: LocalizedStringModel = LocalizedStringModel(),
    val category: CocktailCategory = CocktailCategory.UNDEFINED,
    val alcoholType: CocktailAlcoholType = CocktailAlcoholType.UNDEFINED,
    val glass: CocktailGlassType = CocktailGlassType.UNDEFINED,
    val image: String = "",
    val instructions: LocalizedStringModel = LocalizedStringModel(),
    val ingredients: List<CocktailIngredient> = emptyList(),
    val measures: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val dateAdded: Date = Calendar.getInstance().time
)