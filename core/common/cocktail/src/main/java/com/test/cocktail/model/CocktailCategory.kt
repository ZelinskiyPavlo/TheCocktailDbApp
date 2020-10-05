package com.test.cocktail.model

import com.test.thecocktaildb.presentation.ui.cocktail.filtertype.DrinkFilter
import com.test.thecocktaildb.presentation.ui.cocktail.filtertype.DrinkFilterType

@Suppress("unused")
enum class CocktailCategory(override val type: DrinkFilterType, override val key: String) :
    DrinkFilter {
    ORDINARY_DRINK(DrinkFilterType.CATEGORY, "Ordinary Drink"),
    COCKTAIL(DrinkFilterType.CATEGORY, "Cocktail"),
    MILK_FLOAT_SHAKE(DrinkFilterType.CATEGORY,"Milk / Float / Shake"),
    COCOA(DrinkFilterType.CATEGORY, "Cocoa"),
    SHOT(DrinkFilterType.CATEGORY, "Shot"),
    COFFEE_TEA(DrinkFilterType.CATEGORY, "Coffee / Tea"),
    HOMEMADE_LIQUEUR(DrinkFilterType.CATEGORY, "Homemade Liqueur"),
    PUNCH_PARTY_DRINK(DrinkFilterType.CATEGORY, "Punch / Party Drink"),
    BEER(DrinkFilterType.CATEGORY, "Beer"),
    SOFT_DRINK_SODA(DrinkFilterType.CATEGORY, "Soft Drink / Soda"),
    UNNKNOWN(DrinkFilterType.CATEGORY, "Other/Unknown"),
    UNDEFINED(DrinkFilterType.CATEGORY, "")
}