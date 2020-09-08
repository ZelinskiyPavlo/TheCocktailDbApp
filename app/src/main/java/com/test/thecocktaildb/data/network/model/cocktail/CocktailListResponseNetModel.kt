package com.test.thecocktaildb.data.network.model.cocktail

import com.google.gson.annotations.SerializedName

data class CocktailListResponseNetModel(

    @SerializedName("drinks")
    val drinks: List<CocktailNetModel>?
)