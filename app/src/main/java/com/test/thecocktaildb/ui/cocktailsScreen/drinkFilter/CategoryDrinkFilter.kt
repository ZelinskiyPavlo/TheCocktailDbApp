package com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter

enum class CategoryDrinkFilter(override val type: DrinkFilterType, override val key: String) :
    DrinkFilter {
    ORDINARY_DRINK(DrinkFilterType.CATEGORY, "Ordinary_Drink"),
    COCKTAIL(DrinkFilterType.CATEGORY, "Cocktail"),
    MILK_FLOAT_SHAKE(DrinkFilterType.CATEGORY, "Milk_/_Float_/_Shake"),
    OTHER_UNKNOWN(DrinkFilterType.CATEGORY, "Other/Unknown"),
    COCOA(DrinkFilterType.CATEGORY, "Cocoa"),
    SHOT(DrinkFilterType.CATEGORY, "Shot"),
    COFFEE_TEA(DrinkFilterType.CATEGORY, "Coffee_/_Tea"),
    HOMEMADE_LIQUEUR(DrinkFilterType.CATEGORY, "Homemade Liqueur"),
    PUNCH_PARTY_DRINK(DrinkFilterType.CATEGORY, "Punch_/_Party_Drink"),
    BEER(DrinkFilterType.CATEGORY, "Beer"),
    SOFT_DRINK_SODA(DrinkFilterType.CATEGORY, "Soft Drink_/_Soda"),
}