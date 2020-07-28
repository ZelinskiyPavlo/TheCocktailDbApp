package com.test.thecocktaildb.dataNew.network.model.response

import com.google.gson.annotations.SerializedName

data class TokenNetModel(
    @SerializedName("token")
    val token: String
)