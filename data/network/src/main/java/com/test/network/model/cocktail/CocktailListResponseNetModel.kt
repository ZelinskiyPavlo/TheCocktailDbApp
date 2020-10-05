package com.test.network.model.cocktail

import com.google.gson.annotations.SerializedName

data class CocktailListResponseNetModel(

    @SerializedName("drinks")
    val drinks: List<CocktailNetModel>?
)