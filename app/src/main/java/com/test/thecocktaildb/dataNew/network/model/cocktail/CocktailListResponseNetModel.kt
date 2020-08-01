package com.test.thecocktaildb.dataNew.network.model.cocktail

import com.google.gson.annotations.SerializedName

data class CocktailListResponseNetModel(

    @SerializedName("drinks")
    val drinks: List<CocktailNetModel>?
)