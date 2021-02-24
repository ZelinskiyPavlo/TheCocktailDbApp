package com.test.presentation.model.cocktail.type

import com.test.presentation.model.cocktail.filter.DrinkFilter
import com.test.presentation.model.cocktail.filter.DrinkFilterType

enum class CocktailAlcoholType(override val type: DrinkFilterType, override val key: String) :
    DrinkFilter {
    ALCOHOLIC(DrinkFilterType.ALCOHOL, "Alcoholic"),
    OPTIONAL_ALCOHOL(DrinkFilterType.ALCOHOL, "Optional alcohol"),
    NON_ALCOHOLIC(DrinkFilterType.ALCOHOL, "Non alcoholic"),
    UNDEFINED(DrinkFilterType.ALCOHOL, "")
}