package com.test.thecocktaildb.dataNew.network.model

import com.google.gson.annotations.SerializedName

class UserNetModel(

    @SerializedName("id")
    val id: Long = 1L,

    @SerializedName("email")
    val email: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("lastName")
    val lastName: String = ""
)