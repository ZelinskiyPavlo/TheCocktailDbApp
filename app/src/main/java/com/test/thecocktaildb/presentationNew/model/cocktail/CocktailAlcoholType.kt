package com.test.thecocktaildb.presentationNew.model.cocktail

import com.test.thecocktaildb.ui.cocktailScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailScreen.drinkFilter.DrinkFilterType

@Suppress("unused")
enum class CocktailAlcoholType(override val type: DrinkFilterType, override val key: String) :
    DrinkFilter {
    ALCOHOLIC(DrinkFilterType.ALCOHOL,"Alcoholic"),
    NON_ALCOHOLIC(DrinkFilterType.ALCOHOL,"Non alcoholic"),
    OPTIONAL_ALCOHOL(DrinkFilterType.ALCOHOL,"Optional alcohol"),
    UNDEFINED(DrinkFilterType.ALCOHOL, "")
}