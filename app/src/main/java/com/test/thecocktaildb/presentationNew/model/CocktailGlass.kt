package com.test.thecocktaildb.presentationNew.model

import com.test.thecocktaildb.ui.cocktailScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailScreen.drinkFilter.DrinkFilterType

@Suppress("unused")
enum class CocktailGlass(override val type: DrinkFilterType, override val key: String) :
    DrinkFilter {
    HIGHBALL_GLASS(DrinkFilterType.GLASS, "Highball glass"),
    COCKTAIL_GLASS(DrinkFilterType.GLASS, "Cocktail glass"),
    OLD_FASHIONED_GLASS(DrinkFilterType.GLASS, "Old-fashioned glass"),
    COLLINS_GLASS(DrinkFilterType.GLASS, "Collins glass"),
    POUSSE_CAFE_GLASS(DrinkFilterType.GLASS, "Pousse cafe glass"),
    CHAMPAGNE_FLUTE(DrinkFilterType.GLASS, "Champagne flute"),
    WHISKEY_SOUR_GLASS(DrinkFilterType.GLASS, "Whiskey sour glass"),
    BRANDY_SNIFTER(DrinkFilterType.GLASS, "Brandy snifter"),
    WHITE_WINE_GLASS(DrinkFilterType.GLASS, "White wine glass"),
    NICK_AND_NORA_GLASS(DrinkFilterType.GLASS, "Nick and Nora Glass"),
    HURRICANE_GLASS(DrinkFilterType.GLASS, "Hurricane glass"),
    COFFEE_MUG(DrinkFilterType.GLASS, "Coffee mug"),
    SHOT_GLASS(DrinkFilterType.GLASS, "Shot glass"),
    JAR(DrinkFilterType.GLASS, "Jar"),
    IRISH_COFFEE_CUP(DrinkFilterType.GLASS, "Irish coffee cup"),
    PUNCH_BOWL(DrinkFilterType.GLASS, "Punch bowl"),
    PITCHER(DrinkFilterType.GLASS, "Pitcher"),
    PINT_GLASS(DrinkFilterType.GLASS, "Pint glass"),
    COPPER_MUG(DrinkFilterType.GLASS, "Copper Mug"),
    WINE_GLASS(DrinkFilterType.GLASS, "Wine Glass"),
    CORDIAL_GLASS(DrinkFilterType.GLASS, "Cordial glass"),
    BEER_MUG(DrinkFilterType.GLASS, "Beer mug"),
    MARGARITA_COUPETTE_GLASS(DrinkFilterType.GLASS, "Margarita/Coupette glass"),
    BEER_PILSNER(DrinkFilterType.GLASS, "Beer pilsner"),
    BEER_GLASS(DrinkFilterType.GLASS, "Beer Glass"),
    PARFAIT_GLASS(DrinkFilterType.GLASS, "Parfait glass"),
    MASON_JAR(DrinkFilterType.GLASS, "Mason jar"),
    MARGARITA_GLASS(DrinkFilterType.GLASS, "Margarita glass"),
    MARTINI_GLASS(DrinkFilterType.GLASS, "Martini Glass"),
    BALLOON_GLASS(DrinkFilterType.GLASS, "Balloon Glass"),
    COUPE_GLASS(DrinkFilterType.GLASS, "Coupe Glass"),
    UNDEFINED(DrinkFilterType.GLASS, "")
}