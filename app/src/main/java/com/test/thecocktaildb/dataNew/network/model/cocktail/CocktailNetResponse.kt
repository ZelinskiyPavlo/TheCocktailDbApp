package com.test.thecocktaildb.dataNew.network.model.cocktail

import com.google.gson.annotations.SerializedName

// TODO: це треба повертати з ApiService
class CocktailNetResponse {

    @SerializedName("cocktails")
    var cocktails: List<CocktailNetModel> = arrayListOf()
}