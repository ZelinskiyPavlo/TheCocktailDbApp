package com.test.thecocktaildb.ui.cocktail.filtertype

enum class AlcoholDrinkFilter(override val type: DrinkFilterType, override val key: String) :
    DrinkFilter {
    ALCOHOLIC(DrinkFilterType.ALCOHOL, "Alcoholic"),
    OPTIONAL_ALCOHOL(DrinkFilterType.ALCOHOL, "Optional alcohol"),
    NON_ALCOHOLIC(DrinkFilterType.ALCOHOL, "Non alcoholic")
}