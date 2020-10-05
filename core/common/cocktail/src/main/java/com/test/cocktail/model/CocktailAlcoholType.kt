package com.test.cocktail.model

import com.test.thecocktaildb.presentation.ui.cocktail.filtertype.DrinkFilter
import com.test.thecocktaildb.presentation.ui.cocktail.filtertype.DrinkFilterType

@Suppress("unused")
enum class CocktailAlcoholType(override val type: DrinkFilterType, override val key: String) :
    DrinkFilter {
    ALCOHOLIC(DrinkFilterType.ALCOHOL,"Alcoholic"),
    NON_ALCOHOLIC(DrinkFilterType.ALCOHOL,"Non alcoholic"),
    OPTIONAL_ALCOHOL(DrinkFilterType.ALCOHOL,"Optional alcohol"),
    UNDEFINED(DrinkFilterType.ALCOHOL, "")
}