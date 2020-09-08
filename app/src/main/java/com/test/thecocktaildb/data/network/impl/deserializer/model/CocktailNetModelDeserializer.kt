package com.test.thecocktaildb.data.network.impl.deserializer.model

import com.google.gson.*
import com.test.thecocktaildb.data.network.impl.extension.deserializeType
import com.test.thecocktaildb.data.network.impl.extension.getMemberStringOrEmpty
import com.test.thecocktaildb.data.network.model.cocktail.CocktailNetModel
import com.test.thecocktaildb.data.network.model.cocktail.LocalizedStringNetModel
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.LinkedHashMap

class CocktailNetModelDeserializer : JsonDeserializer<CocktailNetModel> {

    private val dateType = deserializeType<Date>()
    private val ingredientMap = LinkedHashMap<String, String>()

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement?, typeOfT: Type,
        context: JsonDeserializationContext
    ) = json!!.asJsonObject!!.let { jsonObject ->
        groupIngredient(jsonObject)

        CocktailNetModel(
            id = jsonObject.get("idDrink").asLong,
            names = LocalizedStringNetModel(
                default = jsonObject.getMemberStringOrEmpty("strDrink"),
                defaultAlternate = jsonObject.getMemberStringOrEmpty("strDrinkAlternate"),
                es = jsonObject.getMemberStringOrEmpty("strDrinkES"),
                de = jsonObject.getMemberStringOrEmpty("strDrinkDE"),
                fr = jsonObject.getMemberStringOrEmpty("strDrinkFR"),
                zhHans = jsonObject.getMemberStringOrEmpty("strDrinkZH-HANS"),
                zhHant = jsonObject.getMemberStringOrEmpty("strDrinkZH-HANT")
            ),
            category = jsonObject.getMemberStringOrEmpty("strCategory"),
            alcoholType = jsonObject.getMemberStringOrEmpty("strAlcoholic"),
            glass = jsonObject.getMemberStringOrEmpty("strGlass"),
            image = jsonObject.getMemberStringOrEmpty("strDrinkThumb"),
            instructions = LocalizedStringNetModel(
                default = jsonObject.getMemberStringOrEmpty("strInstructions"),
                defaultAlternate = jsonObject.getMemberStringOrEmpty("strInstructionsAlternate"),
                es = jsonObject.getMemberStringOrEmpty("strInstructionsES"),
                de = jsonObject.getMemberStringOrEmpty("strInstructionsDE"),
                fr = jsonObject.getMemberStringOrEmpty("strInstructionsFR"),
                zhHans = jsonObject.getMemberStringOrEmpty("strInstructionsZH-HANS"),
                zhHant = jsonObject.getMemberStringOrEmpty("strInstructionsZH-HANT"),
            ),
            ingredients = ingredientMap.keys.filter { it.isNotEmpty() },
            measures = ingredientMap.values.take(
                ingredientMap.keys.indexOf("")
            ),
            date = when {
                jsonObject.has("dateModified") && !jsonObject.get("dateModified").isJsonNull -> {
                    context.deserialize(jsonObject.get("dateModified"), dateType)
                }
                else -> Date()
            }
        )
    }

    private fun groupIngredient(jsonObject: JsonObject) {
        ingredientMap.clear()
        (1..15).forEach { i ->
            val ingredientKey = jsonObject.getMemberStringOrEmpty("strIngredient${i}")
            val measureValue = jsonObject.getMemberStringOrEmpty("strMeasure${i}")

            ingredientMap[ingredientKey] = measureValue
        }
    }
}