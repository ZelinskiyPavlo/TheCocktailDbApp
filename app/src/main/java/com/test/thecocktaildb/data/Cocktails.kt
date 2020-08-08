package com.test.thecocktaildb.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.test.thecocktaildb.ui.detail.Ingredient
import java.util.*


data class Cocktails(
    @SerializedName("drinks")
    val cocktailsList: List<Cocktail>?
)

@Entity(tableName = "cocktails")
data class Cocktail @JvmOverloads constructor(
    @SerializedName("idDrink")
    @PrimaryKey
    val idDrink: String,

    var dateAdded: Date? = Calendar.getInstance().time,

    val isFavorite: Boolean = false,

    @SerializedName("strAlcoholic")
    val strAlcoholic: String?,
    @SerializedName("strCategory")
    val strCategory: String?,

    @SerializedName("strDrink")
    val strDrink: String,

    @SerializedName("strDrinkThumb")
    val strDrinkThumb: String?,
    @SerializedName("strGlass")
    val strGlass: String?,

    @SerializedName("strIngredient1")
    val strIngredient1: String?,
    @SerializedName("strIngredient2")
    val strIngredient2: String?,
    @SerializedName("strIngredient3")
    val strIngredient3: String?,
    @SerializedName("strIngredient4")
    val strIngredient4: String?,
    @SerializedName("strIngredient5")
    val strIngredient5: String?,
    @SerializedName("strIngredient6")
    val strIngredient6: String?,
    @SerializedName("strIngredient7")
    val strIngredient7: String?,
    @SerializedName("strIngredient8")
    val strIngredient8: String?,
    @SerializedName("strIngredient9")
    val strIngredient9: String?,
    @SerializedName("strIngredient10")
    val strIngredient10: String?,
    @SerializedName("strIngredient11")
    val strIngredient11: String?,
    @SerializedName("strIngredient12")
    val strIngredient12: String?,
    @SerializedName("strIngredient13")
    val strIngredient13: String?,
    @SerializedName("strIngredient14")
    val strIngredient14: String?,
    @SerializedName("strIngredient15")
    val strIngredient15: String?,

    @SerializedName("strInstructions")
    val strInstructions: String?,

    @SerializedName("strMeasure1")
    val strMeasure1: String?,
    @SerializedName("strMeasure2")
    val strMeasure2: String?,
    @SerializedName("strMeasure3")
    val strMeasure3: String?,
    @SerializedName("strMeasure4")
    val strMeasure4: String?,
    @SerializedName("strMeasure5")
    val strMeasure5: String?,
    @SerializedName("strMeasure6")
    val strMeasure6: String?,
    @SerializedName("strMeasure7")
    val strMeasure7: String?,
    @SerializedName("strMeasure8")
    val strMeasure8: String?,
    @SerializedName("strMeasure9")
    val strMeasure9: String?,
    @SerializedName("strMeasure10")
    val strMeasure10: String?,
    @SerializedName("strMeasure11")
    val strMeasure11: String?,
    @SerializedName("strMeasure12")
    val strMeasure12: String?,
    @SerializedName("strMeasure13")
    val strMeasure13: String?,
    @SerializedName("strMeasure14")
    val strMeasure14: String?,
    @SerializedName("strMeasure15")
    val strMeasure15: String?
) {

    fun ingredientsNumber(): Int {
        return with(createIngredientsList()) {
            removeAll{ it.name.isNullOrEmpty() }
            size
        }
    }

    fun createNumberedIngredientsList(): List<Ingredient> {
        val ingredientsList = createIngredientsList()
        ingredientsList.removeAll { it.name.isNullOrEmpty() }
        addSequenceNumber(ingredientsList)
        removeNewLineCharacter(ingredientsList)
        return ingredientsList
    }

    fun createIngredientsList(): MutableList<Ingredient> {
//        maybe better approach would be to somehow get Json string save it in Db and then parse it
//        when populating data
        return mutableListOf<Ingredient>().apply {
            add(Ingredient(strIngredient1, strMeasure1))
            add(Ingredient(strIngredient2, strMeasure2))
            add(Ingredient(strIngredient3, strMeasure3))
            add(Ingredient(strIngredient4, strMeasure4))
            add(Ingredient(strIngredient5, strMeasure5))
            add(Ingredient(strIngredient6, strMeasure6))
            add(Ingredient(strIngredient7, strMeasure7))
            add(Ingredient(strIngredient8, strMeasure8))
            add(Ingredient(strIngredient9, strMeasure9))
            add(Ingredient(strIngredient10, strMeasure10))
            add(Ingredient(strIngredient11, strMeasure11))
            add(Ingredient(strIngredient12, strMeasure12))
            add(Ingredient(strIngredient13, strMeasure13))
            add(Ingredient(strIngredient14, strMeasure14))
            add(Ingredient(strIngredient15, strMeasure15))
        }
    }

    private fun addSequenceNumber(list: MutableList<Ingredient>) {
        list.forEachIndexed { index, ingredient ->
            ingredient.name = index.inc().toString() + ". " + ingredient.name
        }
    }

    private fun removeNewLineCharacter(list: MutableList<Ingredient>) {
        list.forEach { ingredient ->
            if (ingredient.measure?.endsWith("\n") == true) {
                ingredient.measure = ingredient.measure!!.removeSuffix("\n")
            }
            if (ingredient.name?.endsWith("\n") == true) {
                ingredient.name = ingredient.name!!.removeSuffix("\n")
            }
        }
    }
}