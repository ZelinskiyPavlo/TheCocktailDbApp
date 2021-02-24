package com.test.cocktail.receiver.callback

internal interface DrinkProposalCallback {

    fun proposeCocktail(selectedCocktailId: Long)
}